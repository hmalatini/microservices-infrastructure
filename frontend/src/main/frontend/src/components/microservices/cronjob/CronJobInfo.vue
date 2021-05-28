<template>
  <div>
    <div style="position: fixed; top: 50%;" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else>
      <div style="margin: 20px 0">
        <router-link to="cronjob/create">
          <mdb-btn style="background-color: #00A082;">Create New Cronjob</mdb-btn>
        </router-link>
      </div>
      <div v-if="cronJobs.length > 0">
        <mdb-tbl hover>
          <mdb-tbl-head>
            <tr>
              <th>Name</th>
              <th>Environment</th>
              <th>Schedule</th>
              <th>Endpoint</th>
            </tr>
          </mdb-tbl-head>
          <mdb-tbl-body>
            <tr v-for="job in cronJobs" :key="job.name">
              <td>{{ job.name }}</td>
              <td>{{ job.environmentName }}</td>
              <td>{{ job.schedule }}</td>
              <td>{{ job.endpoint }}</td>
              <td>
                <mdb-btn size="sm" color="primary" @click.native="editModal = true, modalCronJob = job, updateCronjobDTO = Object.assign({}, job)">
                  <mdb-icon icon="magic" class="mr-1"/>
                  Edit
                </mdb-btn>
              </td>
              <td>
                <mdb-btn size="sm" color="danger" @click.native="deleteModal = true, modalCronJob = job">
                  <mdb-icon icon="trash" class="mr-1"/>
                  Delete
                </mdb-btn>
              </td>
            </tr>
          </mdb-tbl-body>
        </mdb-tbl>
      </div>
      <!-- EDIT MODAL -->
      <mdb-modal size="md" centered :show="editModal" @close="editModal = false, errors = ''">
        <mdb-modal-header>
          <mdb-modal-title>Edit <strong>{{ modalCronJob.name.toUpperCase() }} - {{ modalCronJob.environmentName.toUpperCase() }}</strong></mdb-modal-title>
        </mdb-modal-header>
        <form v-on:submit.prevent="updateCronJobBackend">
          <mdb-modal-body>
            <label for="changeSchedule" class="grey-text">Schedule</label>
            <input class="form-control" type="text" id="changeSchedule" v-model="updateCronjobDTO.schedule"/>
            <br>
            <label for="changeEndpoint" class="grey-text">Endpoint</label>
            <input class="form-control" type="text" id="changeEndpoint" v-model="updateCronjobDTO.endpoint"/>
          </mdb-modal-body>
          <mdb-modal-footer>
            <mdb-btn style="background-color: #00A082;" size="sm" @click.native="editModal = false, errors = ''">Close</mdb-btn>
            <mdb-btn color="primary" size="sm" v-bind:disabled="!formEnabled" type="submit">Save changes</mdb-btn>
            <br>
            <p class="font-weight-bold" style="color:red">{{ errors }}</p>
          </mdb-modal-footer>
        </form>
      </mdb-modal>
      <!-- DELETE MODAL -->
      <mdb-modal frame position="bottom" direction="bottom" :show="deleteModal" @close="deleteModal = false, errors = ''">
        <mdb-modal-body class="text-center">
          <span>Are you sure you want to delete <strong>{{ modalCronJob.name.toUpperCase() }} - {{ modalCronJob.environmentName.toUpperCase() }}</strong></span>
          <mdb-btn style="background-color: #00A082;" @click.native="deleteModal = false, errors = ''">Close</mdb-btn>
          <mdb-btn v-on:click="deleteCronJobBackend" color="danger">CONFIRM DELETE</mdb-btn>
          <p class="font-weight-bold" style="color:red">{{ errors }}</p>
        </mdb-modal-body>
      </mdb-modal>
    </div>
  </div>
</template>

<script>
import backendHttpClient from "@/plugins/axios";

function getCronJobListFromResponse(cronJobResponse) {
  let cronJobList = []

  cronJobResponse.data.forEach(cronJob => {
    cronJobList.push(cronJob)
  })

  return cronJobList;
}

function validateSchedule(cron) {
  if (!/(@(annually|yearly|monthly|weekly|daily|hourly|reboot))|(@every (\d+(ns|us|µs|ms|s|m|h))+)|((((\d+,)+\d+|([\d*]+(\/|-)\d+)|\d+|\*) ?){5,7})/.test(
      cron)) {
    return 'Invalid cron expresion in schedule field. Valid: https://en.wikipedia.org/wiki/Cron'
  }

  return ''
}

function validateEndpoint(endpoint) {
  if (endpoint === undefined || endpoint.length <= 0){
    return 'You must provide an endpoint.'
  } else if (endpoint.charAt(0) !== '/') {
    return 'Invalid endpoint. It should start with \'/\''
  }

  return ''
}

export default {
  name: "CronJobInfo",
  data() {
    return {
      cronJobs: [],
      editModal: false,
      deleteModal: false,
      modalCronJob: {name: '', environmentName: ''},
      updateCronjobDTO: {},
      pendingResponse: false,
      formEnabled: true,
      errors: '',
    }
  },
  created: function () {
    this.getCronJobs()
  },
  methods: {
    isThereErrors() {
      return this.errors.length > 0
    },
    validateUpdateCronJob() {
      if (this.updateCronjobDTO.schedule === undefined || this.updateCronjobDTO.schedule === '') {
        this.errors = 'You must provide a schedule'
      }

      if (this.updateCronjobDTO.endpoint === undefined || this.updateCronjobDTO.endpoint === '') {
        this.errors = 'You must provide an endpoint'
      }

      this.errors = validateSchedule(this.updateCronjobDTO.schedule)
      if (this.isThereErrors()) {
        return;
      }

      this.errors = validateEndpoint(this.updateCronjobDTO.endpoint)
    },
    updateCronJobBackend() {
      this.formEnabled = false
      this.editModal = false
      this.errors = ''
      this.validateUpdateCronJob()

      if (!this.isThereErrors()) {
        this.pendingResponse = true

        const body = {
          schedule: this.updateCronjobDTO.schedule,
          endpoint: this.updateCronjobDTO.endpoint,
        }

        const serviceName = this.$route.params.microserviceName
        const envName = this.updateCronjobDTO.environmentName
        backendHttpClient.put(serviceName + '/' + envName + '/cronjob/' + this.updateCronjobDTO.name, body).then(() => {
          this.getCronJobs()
          this.formEnabled = true
          this.editModal = false
          this.pendingResponse = false
        })
        .catch(() => {
          this.errors = 'Error updating cronjob'
          this.formEnabled = true
          this.editModal = true
          this.pendingResponse = false
        })
      } else {
        this.formEnabled = true
        this.editModal = true
      }
    },
    deleteCronJobBackend() {
      this.deleteModal = false
      this.pendingResponse = true
      this.errors = ''

      const serviceName = this.$route.params.microserviceName
      const envName = this.modalCronJob.environmentName
      backendHttpClient.delete(serviceName + '/' + envName + '/cronjob/' + this.modalCronJob.name).then(() => {
        this.getCronJobs()
        this.deleteModal = false
        this.pendingResponse = false
      })
      .catch(() => {
        this.errors = 'Error deleting cronjob'
        this.deleteModal = true
        this.pendingResponse = false
      })
    },
    getCronJobs() {
      const serviceName = this.$route.params.microserviceName
      backendHttpClient.get(serviceName + '/cronjob').then(cronJobResponse => {
        this.cronJobs = getCronJobListFromResponse(cronJobResponse)
      })
    }
  }
}
</script>

<style scoped>

</style>