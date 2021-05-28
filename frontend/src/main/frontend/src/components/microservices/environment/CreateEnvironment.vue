<template>
  <div>
    <div id="pending-spinner" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else id="form">
      <form v-on:submit.prevent="submitForm">
        <p class="h4 text-center mb-4">Create New Environment</p>
        <label for="environmentName" class="grey-text">Environment Name</label>
        <br>
        <input class="form-control" type="text" id="environmentName" v-model="environmentName"/>
        <label for="isTest" class="grey-text">Is test?</label><input class="form-control" type="checkbox" id="isTest" v-model="isTest"/>
        <br>
        <label for="releaseName" class="grey-text">Release to deploy on it</label>
        <select class="browser-default custom-select" id="releaseName" v-model="release">
          <option value="" disabled selected>Select a release</option>
          <option v-for="r in releaseNames" :key="r" :value="r">{{ r }}</option>
        </select>
        <label for="clusterName" class="grey-text">Cluster to be deployed into</label>
        <select class="browser-default custom-select" id="clusterName" v-model="cluster">
          <option value="" disabled selected>Select a cluster</option>
          <option v-for="c in clusterNames" :key="c" :value="c">{{ c }}</option>
        </select>
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

function nameContainInvalidCharacters(serviceName) {
  return !/^[a-z-]+$/.test(serviceName);
}

function validateEnvironmentName(serviceName) {
  if (serviceName == null || serviceName.length <= 3) {
    return 'Environment name has to be greater than 3 characters'
  } else if (nameContainInvalidCharacters(serviceName)) {
    return 'Environment name cant contain spaces nor numbers. Only dash \'-\' are allowed.'
  }

  return ''
}

function validateClusters(cluster, clusterNames) {
  if (clusterNames.indexOf(cluster) === -1) {
    return 'Invalid cluster selected'
  }

  return ''
}

function validateReleases(release, releaseNames) {
  if (releaseNames.indexOf(release) === -1) {
    return 'Invalid release selected'
  }

  return ''
}

function getReleaseNamesFromResponse(environmentResponse) {
  let releaseList = []

  environmentResponse.data.releaseVersionNames.forEach(release => {
    releaseList.push(release)
  })

  return releaseList;
}

function getClusterNamesFromResponse(clusterResponse) {
  let clusterList = []

  clusterResponse.data.forEach(cluster => {
    clusterList.push(cluster.name)
  })

  return clusterList;
}

export default {
  name: "CreateEnvironment",
  data() {
    return {
      pendingResponse: false,
      formEnabled: true,
      errors: '',
      environmentName: '',
      releaseNames: [],
      clusterNames: [],
      release: '',
      cluster: '',
      isTest: false,
    };
  },
  created: function () {
    this.getReleases()
    this.getClusters()
  },
  methods: {
    isThereErrors() {
      return this.errors.length > 0
    },
    validate() {
      this.errors = validateEnvironmentName(this.environmentName)
      if (this.isThereErrors()) {
        return;
      }

      this.errors = validateClusters(this.cluster, this.clusterNames)
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
          environmentName: this.environmentName,
          imageVersion: 'service-name-' + serviceName + '-version-' + this.release,
          clusterName: this.cluster,
          isTest: this.isTest,
        }


        backendHttpClient.post(serviceName + '/environment', body).then(() => {
          const serviceName = this.$route.params.microserviceName
          router.push({path: `/microservices/${serviceName}/environment` })
        })
        .catch(() => {
          this.errors = 'Error creating new environment'
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
    getClusters() {
      backendHttpClient.get('cluster').then(clusterResponse => {
        this.clusterNames = getClusterNamesFromResponse(clusterResponse)
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