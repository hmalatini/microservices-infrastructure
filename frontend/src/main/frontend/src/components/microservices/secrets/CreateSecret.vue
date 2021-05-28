<template>
  <div>
    <div id="pending-spinner" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else id="form">
      <form v-on:submit.prevent="submitForm">
        <p class="h4 text-center mb-4">Create New Secret</p>
        <label for="secretName" class="grey-text">Name</label>
        <input class="form-control" type="text" id="secretName" v-model="secretName"/>
        <br>
        <label for="secretValue" class="grey-text">Value</label>
        <input class="form-control" type="text" id="secretValue" v-model="secretValue"/>
        <br>
        <label for="secretDescription" class="grey-text">Description</label>
        <input class="form-control" type="text" id="secretDescription" v-model="secretDescription"/>
        <br>
        <label for="isSensitive" class="grey-text">Is sensitive?</label>
        <input class="form-control" type="checkbox" id="isSensitive" v-model="isSensitive"/>
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

export default {
  name: "CreateSecret",
  data() {
    return {
      pendingResponse: false,
      formEnabled: true,
      errors: '',
      secretName: '',
      secretValue: '',
      secretDescription: '',
      isSensitive: false,
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
          name: this.secretName,
          value: this.secretValue,
          description: this.secretDescription,
          isSensitive: this.isSensitive,
        }

        backendHttpClient.post(serviceName + '/secrets', body).then(() => {
          const serviceName = this.$route.params.microserviceName
          router.push({path: `/microservices/${serviceName}/secrets`})
        })
        .catch(() => {
          this.errors = 'Error creating new secret'
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