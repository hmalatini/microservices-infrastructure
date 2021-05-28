<template>
  <div>
    <div id="pending-spinner" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else id="form">
      <form v-on:submit.prevent="submitForm">
        <p class="h4 text-center mb-4">Create New Microservice</p>
        <label for="serviceName" class="grey-text">Microservice Name</label>
        <input class="form-control" type="text" id="serviceName" label="Microservice Name" v-model="serviceName"/>
        <br>
        <label for="baseTemplate" class="grey-text">Base Template</label>
        <select class="browser-default custom-select" id="baseTemplate" v-model="serviceTemplate">
          <option value="" disabled selected>Select a base template</option>
          <option value="spring-maven-template">Java with Spring and Maven</option>
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

function validateServiceName(serviceName) {
  if (serviceName == null || serviceName.length <= 3) {
    return 'Service name has to be greater than 3 characters'
  } else if (nameContainInvalidCharacters(serviceName)) {
    return 'Service name cant contain spaces nor numbers. Only dash \'-\' are allowed.'
  }

  return ''
}

function validateTemplate(serviceTemplate) {
  if (serviceTemplate !== "spring-maven-template") {
    return 'Select a valid base template'
  }

  return ''
}

export default {
  name: "CreateMicroservice",
  data() {
    return {
      pendingResponse: false,
      formEnabled: true,
      errors: '',
      serviceName: '',
      serviceTemplate: '',
    };
  },
  methods: {
    isThereErrors() {
      return this.errors.length > 0
    },
    validate() {
      this.errors = validateServiceName(this.serviceName)
      if (this.isThereErrors()) {
        return;
      }

      this.errors = validateTemplate(this.serviceTemplate)
    },
    submitForm() {
      this.formEnabled = false
      this.errors = ''
      this.validate()

      if (!this.isThereErrors()) {
        this.pendingResponse = true

        const body = {
          name: this.serviceName,
          template: this.serviceTemplate
        }

        backendHttpClient.post('microservice', body).then(response => {
          console.log(response)
          router.push({path: `/microservices/${response.data.name}/info` })
        })
        .catch(error => {
          console.log(error)
          this.errors = 'Error creating new microservice'
          this.formEnabled = true
          this.pendingResponse = false
        })
      } else {
        this.formEnabled = true
      }
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