<template>
  <div>
    <div style="position: fixed; top: 50%;" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else>
      <div style="margin: 20px 0">
        <router-link to="secrets/create">
          <mdb-btn style="background-color: #00A082;">Create New Secret</mdb-btn>
        </router-link>
      </div>
      <div v-if="secrets.length > 0">
        <mdb-tbl hover>
          <mdb-tbl-head>
            <tr>
              <th>Name</th>
              <th>Value</th>
              <th>Description</th>

            </tr>
          </mdb-tbl-head>
          <mdb-tbl-body>
            <tr v-for="secret in secrets" :key="secret.name">
              <td>{{ secret.name }}</td>
              <td>{{ secret.value }}</td>
              <td>{{ secret.description }}</td>
              <td>
                <mdb-btn size="sm" color="primary" @click.native="editModal = true, modalSecret = secret, updateSecretDTO = Object.assign({}, secret)">
                  <mdb-icon icon="magic" class="mr-1"/>
                  Edit
                </mdb-btn>
              </td>
              <td>
                <mdb-btn size="sm" color="danger" @click.native="deleteModal = true, modalSecret = secret">
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
          <mdb-modal-title>Edit <strong>{{ modalSecret.name.toUpperCase() }}</strong></mdb-modal-title>
        </mdb-modal-header>
        <form v-on:submit.prevent="updateSecretBackend">
          <mdb-modal-body>
            <label for="changeValue" class="grey-text">Value</label>
            <input class="form-control" type="text" id="changeValue" v-model="updateSecretDTO.value"/>
            <br>
            <label for="changeDescription" class="grey-text">Description</label>
            <input class="form-control" type="text" id="changeDescription" v-model="updateSecretDTO.description"/>
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
        <span>Are you sure you want to delete <strong>{{ modalSecret.name.toUpperCase() }}</strong></span>
          <mdb-btn style="background-color: #00A082;" @click.native="deleteModal = false, errors = ''">Close</mdb-btn>
          <mdb-btn v-on:click="deleteSecret" color="danger">CONFIRM DELETE</mdb-btn>
          <p class="font-weight-bold" style="color:red">{{ errors }}</p>
        </mdb-modal-body>
      </mdb-modal>
    </div>
  </div>
</template>

<script>

import backendHttpClient from "@/plugins/axios";

function getSecretListFromResponse(secretResponse) {
  let secretList = []

  secretResponse.data.forEach(secret => {
    secretList.push(secret)
  })

  return secretList;
}

export default {
  name: "SecretsInfo",
  data() {
    return {
      secrets: [],
      editModal: false,
      deleteModal: false,
      modalSecret: {name: ''},
      updateSecretDTO: {},
      pendingResponse: false,
      formEnabled: true,
      errors: '',
    }
  },
  created: function () {
    this.getSecrets()
  },
  methods: {
    isThereErrors() {
      return this.errors.length > 0
    },
    validateUpdateSecret() {
      if (this.updateSecretDTO.value === undefined || this.updateSecretDTO.value === '') {
        this.errors = 'You must provide a value'
      }
    },
    updateSecretBackend() {
      this.formEnabled = false
      this.editModal = false
      this.errors = ''
      this.validateUpdateSecret()

      if (!this.isThereErrors()) {
        this.pendingResponse = true

        const body = {
          value: this.updateSecretDTO.value,
          description: this.updateSecretDTO.description,
        }

        const serviceName = this.$route.params.microserviceName
        backendHttpClient.put(serviceName + '/secrets/' + this.updateSecretDTO.name, body).then(() => {
          this.getSecrets()
          this.formEnabled = true
          this.editModal = false
          this.pendingResponse = false
        })
        .catch(() => {
          this.errors = 'Error updating secret'
          this.formEnabled = true
          this.editModal = true
          this.pendingResponse = false
        })
      } else {
        this.formEnabled = true
      }
    },
    deleteSecret() {
      this.deleteModal = false
      this.pendingResponse = true
      this.errors = ''

      const serviceName = this.$route.params.microserviceName
      backendHttpClient.delete(serviceName + '/secrets/' + this.modalSecret.name).then(() => {
        this.getSecrets()
        this.deleteModal = false
        this.pendingResponse = false
      })
      .catch(() => {
        this.errors = 'Error deleting secret'
        this.deleteModal = true
        this.pendingResponse = false
      })
    },
    getSecrets() {
      const serviceName = this.$route.params.microserviceName
      backendHttpClient.get(serviceName + '/secrets/all').then(secretResponse => {
        this.secrets = getSecretListFromResponse(secretResponse)
      })
    }
  }
}
</script>

<style scoped>

</style>