<template>
  <div>
    <div style="position: fixed; top: 50%;" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else>
      <mdb-btn size="lg" color="danger" style="margin-top: 20px" @click.native="deleteModal = true">
        <mdb-icon icon="trash" class="mr-1"/>
        Delete
      </mdb-btn>
      <!-- DELETE MODAL -->
      <mdb-modal frame position="bottom" direction="bottom" :show="deleteModal" @close="deleteModal = false, errors = ''">
        <mdb-modal-body class="text-center">
        <span>Are you sure you want to delete <strong>{{
            serviceName.toUpperCase()
          }}</strong></span>
          <mdb-btn style="background-color: #00A082;" @click.native="deleteModal = false">Close</mdb-btn>
          <mdb-btn v-on:click="deleteMicroservice" color="danger">CONFIRM DELETE</mdb-btn>
          <p class="font-weight-bold" style="color:red">{{ errors }}</p>
        </mdb-modal-body>
      </mdb-modal>
    </div>
  </div>
</template>

<script>
import backendHttpClient from "@/plugins/axios";
import router from "@/router";

export default {
  name: "ConfigInfo",
  data() {
    return {
      serviceName: this.$route.params.microserviceName,
      deleteModal: false,
      pendingResponse: false,
      errors: '',
    }
  },
  methods: {
    deleteMicroservice() {
      this.deleteModal = false
      this.pendingResponse = true
      this.errors = ''

      const serviceName = this.$route.params.microserviceName
      backendHttpClient.delete('microservice/' + serviceName).then(() => {
        this.deleteModal = false
        this.pendingResponse = false
        router.push({path: `/` })
      })
      .catch(error => {
        console.log(error)
        this.errors = 'Error deleting microservice'
        this.deleteModal = true
        this.pendingResponse = false
      })
    },
  }
}
</script>

<style scoped>

</style>