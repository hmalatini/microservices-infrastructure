<template>
  <div>
    <div style="position: fixed; top: 50%;" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else>
      <div style="margin: 20px 0">
        <router-link to="environment/create">
          <mdb-btn style="background-color: #00A082;">Create New Environment</mdb-btn>
        </router-link>
      </div>
      <div class="d-flex align-items-start justify-content-center flex-wrap">
        <div v-for="env in environments" :key="env.name" class="env">
          <p class="env-title">{{ env.name }}</p>
          <div v-if="env.test">
            <p class="env-scope-test">Test</p>
          </div>
          <div v-if="!env.test">
            <p class="env-scope-prod">Productive</p>
          </div>
          <p>Cluster: {{ env.clusterName }}</p>
          <div v-if="env.autoscalingEnabled">
            <p class="env-scope-autoscaling-enabled">Autoscaling Enabled</p>
          </div>
          <div v-if="!env.autoscalingEnabled">
            <p class="env-scope-autoscaling-disabled">Autoscaling Disabled</p>
          </div>
          <div>
            <p class="scale-info">Min Instances: {{ env.minReplicas }}</p>
            <p class="scale-info">Max Instances: {{ env.maxReplicas }}</p>
            <p class="scale-info">CPU Target: {{ env.autoscalingTargetCPU }}%</p>
          </div>
          <div style="margin-top: 25px">
            <mdb-btn size="sm" color="primary" @click.native="editModal = true, modalEnv = env, updateEnv = env">
              <mdb-icon icon="magic" class="mr-1"/>
              Edit
            </mdb-btn>
            <mdb-btn size="sm" color="danger" @click.native="deleteModal = true, modalEnv = env">
              <mdb-icon icon="trash" class="mr-1"/>
              Delete
            </mdb-btn>
          </div>
        </div>
      </div>
    </div>
    <!-- EDIT MODAL -->
    <mdb-modal size="md" centered :show="editModal" @close="editModal = false, errors = ''">
      <mdb-modal-header>
        <mdb-modal-title>Edit <strong>{{ modalEnv.name.toUpperCase() }}</strong></mdb-modal-title>
      </mdb-modal-header>
      <form v-on:submit.prevent="updateEnvironment">
        <mdb-modal-body>

          <label for="enableAutoscaling" class="grey-text">Enable Autoscaling</label>
          <input class="form-control" type="checkbox" id="enableAutoscaling" label="Enable Autoscaling"
                 v-model="updateEnv.autoscalingEnabled"/>
          <label for="minInstances" class="grey-text">Min Instances</label>
          <input class="form-control" type="number" id="minInstances" label="Min Instances"
                 v-model="updateEnv.minReplicas"/>
          <label for="maxInstances" class="grey-text">Max Instances</label>
          <input class="form-control" type="number" id="maxInstances" label="Max Instances"
                 v-model="updateEnv.maxReplicas"/>
          <label for="cupTarget" class="grey-text">CPU Target</label>
          <input class="form-control" type="number" id="cupTarget" label="CPU Target"
                 v-model="updateEnv.autoscalingTargetCPU"/>
        </mdb-modal-body>
        <mdb-modal-footer>
          <mdb-btn style="background-color: #00A082;" size="sm" @click.native="editModal = false">Close</mdb-btn>
          <mdb-btn color="primary" size="sm" v-bind:disabled="!formEnabled" type="submit">Save changes</mdb-btn>
          <br>
          <p class="font-weight-bold" style="color:red">{{ errors }}</p>
        </mdb-modal-footer>
      </form>
    </mdb-modal>
    <!-- DELETE MODAL -->
    <mdb-modal frame position="bottom" direction="bottom" :show="deleteModal" @close="deleteModal = false, errors = ''">
      <mdb-modal-body class="text-center">
        <span>Are you sure you want to delete <strong>{{
            modalEnv.name.toUpperCase()
          }} &emsp; &emsp; &emsp; &emsp;</strong></span>
        <mdb-btn style="background-color: #00A082;" @click.native="deleteModal = false">Close</mdb-btn>
        <mdb-btn v-on:click="deleteEnvironment" color="danger">CONFIRM DELETE</mdb-btn>
        <p class="font-weight-bold" style="color:red">{{ errors }}</p>
      </mdb-modal-body>
    </mdb-modal>
  </div>
</template>

<script>
import backendHttpClient from "@/plugins/axios";

function getEnvironmentList(environmentResponse) {
  let environmentList = []

  environmentResponse.data.forEach(environment => {
    environmentList.push(environment)
  })

  return environmentList;
}

export default {
  name: "EnvironmentInfo",
  data() {
    return {
      environments: [],
      editModal: false,
      deleteModal: false,
      modalEnv: {name: ''},
      updateEnv: {},
      pendingResponse: false,
      formEnabled: true,
      errors: '',
    }
  },
  created: function () {
    this.getEnvironments()
  },
  methods: {
    isThereErrors() {
      return this.errors.length > 0
    },
    validateUpdateEnv() {
      if (this.updateEnv.minReplicas > 100 || this.updateEnv.maxReplicas > 100) {
        this.errors = 'Number of instances lower than 100'
      } else if (this.updateEnv.minReplicas > this.updateEnv.maxReplicas) {
        this.errors = 'Min number has to be lower than max number'
      } else if (this.updateEnv.autoscalingTargetCPU < 0 || this.updateEnv.autoscalingTargetCPU > 100) {
        this.errors = 'CPU Target has to be between 0 and 100'
      }
    },
    updateEnvironment() {
      this.formEnabled = false
      this.editModal = false
      this.errors = ''
      this.validateUpdateEnv()

      if (!this.isThereErrors()) {
        this.pendingResponse = true

        const body = {
          autoscalingEnabled: this.updateEnv.autoscalingEnabled,
          autoscalingTargetCPU: this.updateEnv.autoscalingTargetCPU,
          minReplicas: this.updateEnv.minReplicas,
          maxReplicas: this.updateEnv.maxReplicas,
        }

        const serviceName = this.$route.params.microserviceName
        backendHttpClient.put(serviceName + '/environment/' + this.updateEnv.name, body).then(() => {
          this.getEnvironments()
          this.formEnabled = true
          this.editModal = false
          this.pendingResponse = false
        })
        .catch(error => {
          console.log(error)
          this.errors = 'Error updating environment'
          this.formEnabled = true
          this.editModal = true
          this.pendingResponse = false
        })
      } else {
        this.formEnabled = true
      }
    },
    deleteEnvironment() {
      this.deleteModal = false
      this.pendingResponse = true
      this.errors = ''

      const serviceName = this.$route.params.microserviceName
      backendHttpClient.delete(serviceName + '/environment/' + this.modalEnv.name).then(() => {
        this.getEnvironments()
        this.deleteModal = false
        this.pendingResponse = false
      })
      .catch(error => {
        console.log(error)
        this.errors = 'Error deleting environment'
        this.deleteModal = true
        this.pendingResponse = false
      })
    },
    getEnvironments() {
      const serviceName = this.$route.params.microserviceName
      backendHttpClient.get(serviceName + '/environment').then(environmentResponse => {
        this.environments = getEnvironmentList(environmentResponse)
        console.log(this.environments)
      })
    }
  }
}
</script>

<style scoped>
.env {
  height: 350px;
  width: 300px;

  margin: 20px;
  padding: 5px;
  background: #FFFFFF 0 0 no-repeat padding-box;
  box-shadow: 0 3px 6px #00000029;
  border-radius: 5px;
  opacity: 1;
}

.env-title {
  font-size: 32px;
}

.env-scope-test {
  margin-top: -15px;
  font-size: 20px;
  font-weight: bold;
  color: #F2CC38;
}

.env-scope-prod {
  margin-top: -15px;
  font-size: 20px;
  font-weight: bold;
  color: #00A082;
}

.env-scope-autoscaling-enabled {
  margin-top: 5px;
  font-weight: bold;
  color: #00b300;
}

.env-scope-autoscaling-disabled {
  margin-top: 5px;
  font-weight: bold;
  color: #ff4d4c;
}

.scale-info {
  margin-bottom: 3px;
}
</style>