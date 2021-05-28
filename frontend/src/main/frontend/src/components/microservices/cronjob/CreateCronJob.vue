<template>
  <div>
    <div id="pending-spinner" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else id="form">
      <form v-on:submit.prevent="submitForm">
        <p class="h4 text-center mb-4">Create New CronJob</p>
        <label for="cronJobName" class="grey-text">Name</label>
        <input class="form-control" type="text" id="cronJobName" v-model="cronJobName"/>
        <br>
        <label for="environmentName" class="grey-text">Environment</label>
        <select class="browser-default custom-select" id="environmentName" v-model="environment">
          <option value="" disabled selected>Select an environment</option>
          <option v-for="e in environmentNames" :key="e" :value="e">{{ e }}</option>
        </select>
        <br>
        <label for="cronJobSchedule" class="grey-text">Schedule</label>
        <input class="form-control" type="text" id="cronJobSchedule" v-model="cronJobSchedule"/>
        <br>
        <label for="cronJobEndpoint" class="grey-text">Endpoint</label>
        <input class="form-control" type="text" id="cronJobEndpoint" v-model="cronJobEndpoint"/>
        <br>

        <div class="text-center mt-4">
          <input v-bind:disabled="!formEnabled" value="CREATE" class="btn" style="background-color: #00A082;" type="submit">
        </div>
      </form>
      <p class="font-weight-bold" style="color:red">{{ errors }}</p>
    </div>
  </div>
</template>

<script>
import backendHttpClient from "@/plugins/axios";
import router from "@/router";

function validateEnvironments(environment, environmentNames) {
  if (environmentNames.indexOf(environment) === -1) {
    return 'Invalid environment selected'
  }

  return ''
}

function validateName(name) {
  if (name == null || name.length <= 3) {
    return 'CronJob name has to be greater than 3 characters'
  } else if (!/^[a-z-]+$/.test(name)) {
    return 'CronJob name cant contain spaces nor numbers. Only dash \'-\' are allowed.'
  }

  return ''
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

function getEnvironmentNamesFromResponse(environmentResponse) {
  let environmentList = []

  environmentResponse.data.forEach(environment => {
    environmentList.push(environment.name)
  })

  return environmentList;
}

export default {
  name: "CreateCronJob",
  data() {
    return {
      pendingResponse: false,
      formEnabled: true,
      errors: '',
      cronJobName: '',
      environment: '',
      cronJobSchedule: '',
      cronJobEndpoint: '',
      environmentNames: [],
    };
  },
  created: function () {
    this.getEnvironments()
  },
  methods: {
    isThereErrors() {
      return this.errors.length > 0
    },
    validate() {

      this.errors = validateEnvironments(this.environment, this.environmentNames)
      if (this.isThereErrors()) {
        return;
      }

      this.errors = validateName(this.cronJobName)
      if (this.isThereErrors()) {
        return;
      }

      this.errors = validateSchedule(this.cronJobSchedule)
      if (this.isThereErrors()) {
        return;
      }

      this.errors = validateEndpoint(this.cronJobEndpoint)
    },
    submitForm() {
      this.formEnabled = false
      this.errors = ''
      this.validate()

      if (!this.isThereErrors()) {
        this.pendingResponse = true
        const serviceName = this.$route.params.microserviceName
        const envName = this.environment

        const body = {
          name: this.cronJobName,
          schedule: this.cronJobSchedule,
          endpoint: this.cronJobEndpoint,
        }

        backendHttpClient.post(serviceName + '/' + envName + '/cronjob', body).then(() => {
          const serviceName = this.$route.params.microserviceName
          router.push({path: `/microservices/${serviceName}/cronjob`})
        })
        .catch(() => {
          this.errors = 'Error creating new cronjob'
          this.formEnabled = true
          this.pendingResponse = false
        })
      } else {
        this.formEnabled = true
      }
    },
    getEnvironments() {
      const serviceName = this.$route.params.microserviceName
      backendHttpClient.get(serviceName + '/environment').then(environmentResponse => {
        this.environmentNames = getEnvironmentNamesFromResponse(environmentResponse)
      })
    }
  }
}
</script>

<style scoped>
#pending-spinner {
  position: fixed;
  top: 50%;
}
#form {
  padding: 15px 30%;
}
</style>