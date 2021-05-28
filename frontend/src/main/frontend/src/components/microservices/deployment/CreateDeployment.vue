<template>
  <div>
    <div id="pending-spinner" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else id="form">
      <form v-on:submit.prevent="submitForm">
        <p class="h4 text-center mb-4">Create New Deployment</p>
        <label for="environmentName" class="grey-text">Environment</label>
        <select class="browser-default custom-select" id="environmentName" v-model="environment">
          <option value="" disabled selected>Select an environment</option>
          <option v-for="e in environmentNames" :key="e" :value="e">{{ e }}</option>
        </select>
        <br>
        <label for="releaseName" class="grey-text">Release</label>
        <select class="browser-default custom-select" id="releaseName" v-model="release">
          <option value="" disabled selected>Select a release</option>
          <option v-for="r in releaseNames" :key="r" :value="r">{{ r }}</option>
        </select>
        <div class="text-center mt-4">
          <input v-bind:disabled="!formEnabled" value="CREATE" class="btn" style="background-color: #00A082;"
                 type="submit">
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

function validateReleases(release, releaseNames) {
  if (releaseNames.indexOf(release) === -1) {
    return 'Invalid release selected'
  }

  return ''
}

function getReleaseNamesFromResponse(releaseResponse) {
  let releaseList = []

  releaseResponse.data.releaseVersionNames.forEach(release => {
    releaseList.push(release)
  })

  return releaseList;
}

function getEnvironmentNamesFromResponse(environmentResponse) {
  let environmentList = []

  environmentResponse.data.forEach(environment => {
    environmentList.push(environment.name)
  })

  return environmentList;
}

export default {
  name: "CreateDeployment",
  data() {
    return {
      pendingResponse: false,
      formEnabled: true,
      errors: '',
      releaseNames: [],
      environmentNames: [],
      release: '',
      environment: '',
    };
  },
  created: function () {
    this.getReleases()
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

      this.errors = validateReleases(this.release, this.releaseNames)
    },
    submitForm() {
      this.formEnabled = false
      this.errors = ''
      this.validate()

      if (!this.isThereErrors()) {
        this.pendingResponse = true
        const serviceName = this.$route.params.microserviceName

        const body = {
          serviceName: serviceName,
          environment: this.environment,
          version: 'service-name-' + serviceName + '-version-' + this.release,
        }

        backendHttpClient.post('deployment', body).then(() => {
          const serviceName = this.$route.params.microserviceName
          router.push({path: `/microservices/${serviceName}/environment`})
        })
        .catch(() => {
          this.errors = 'Error creating deploy'
          this.formEnabled = true
          this.pendingResponse = false
        })
      } else {
        this.formEnabled = true
      }
    },
    getReleases() {
      const serviceName = this.$route.params.microserviceName
      backendHttpClient.get(serviceName + '/releases').then(releaseResponse => {
        this.releaseNames = getReleaseNamesFromResponse(releaseResponse)
      })
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