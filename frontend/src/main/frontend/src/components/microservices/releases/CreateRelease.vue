<template>
  <div>
    <div id="pending-spinner" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else id="form">
      <form v-on:submit.prevent="submitForm">
        <p class="h4 text-center mb-4">Create New Release</p>
        <label for="releaseName" class="grey-text">Name</label>
        <input class="form-control" type="text" id="releaseName" v-model="releaseName"/>
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

export default {
  name: "CreateRelease",
  data() {
    return {
      pendingResponse: false,
      formEnabled: true,
      errors: '',
      releaseName: '',
    };
  },
  methods: {
    isThereErrors() {
      return this.errors.length > 0
    },
    submitForm() {
      this.formEnabled = false
      this.errors = ''

      if (!this.isThereErrors()) {
        this.pendingResponse = true
        const serviceName = this.$route.params.microserviceName

        const body = {
          name: this.releaseName,
        }

        backendHttpClient.post(serviceName + '/releases', body).then(() => {
          const serviceName = this.$route.params.microserviceName
          router.push({path: `/microservices/${serviceName}/releases`})
        })
        .catch(() => {
          this.errors = 'Error creating new release'
          this.formEnabled = true
          this.pendingResponse = false
        })
      } else {
        this.formEnabled = true
      }
    },
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