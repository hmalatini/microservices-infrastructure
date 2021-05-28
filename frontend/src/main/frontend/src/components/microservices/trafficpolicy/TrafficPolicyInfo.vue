<template>
  <div>
    <div style="position: fixed; top: 50%;" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else>
      <div style="margin: 20px 0">
        <mdb-btn size="lg" style="background-color: #00A082 !important;"
                 @click.native="createModal = true, createDTO = Object.assign({}, defaultDTO)">
          Create New Traffic Policy
        </mdb-btn>
      </div>
      <div v-if="trafficPolicies.length > 0">
        <mdb-tbl hover>
          <mdb-tbl-head>
            <tr>
              <th>Name</th>
              <th>Environment</th>
              <th>Load Balancer</th>
              <th>Circuit Breaker</th>
            </tr>
          </mdb-tbl-head>
          <mdb-tbl-body>
            <tr v-for="entity in trafficPolicies" :key="entity.name">
              <td>{{ entity.name }}</td>
              <td>{{ entity.environment }}</td>
              <td v-if="entity.loadBalancer">
                <mdb-icon icon="check" class="mr-3"/>
              </td>
              <td v-else>
                <mdb-icon icon="times" class="mr-3"/>
              </td>
              <td v-if="entity.circuitBreaker">
                <mdb-icon icon="check" class="mr-3"/>
              </td>
              <td v-else>
                <mdb-icon icon="times" class="mr-3"/>
              </td>
              <td>
                <mdb-btn size="sm" color="primary"
                         @click.native="editModal = true, modalDTO = entity, updateDTO = Object.assign({}, entity)">
                  <mdb-icon icon="magic" class="mr-1"/>
                  Edit
                </mdb-btn>
              </td>
              <td>
                <mdb-btn size="sm" color="danger" @click.native="deleteModal = true, modalDTO = entity">
                  <mdb-icon icon="trash" class="mr-1"/>
                  Delete
                </mdb-btn>
              </td>
            </tr>
          </mdb-tbl-body>
        </mdb-tbl>
      </div>
      <!-- CREATE MODAL -->
      <mdb-modal size="md" centered :show="createModal" @close="createModal = false, errors = ''">
        <mdb-modal-header>
          <mdb-modal-title>Create new Traffic Policy</mdb-modal-title>
        </mdb-modal-header>
        <form v-on:submit.prevent="createData">
          <mdb-modal-body>
            <label for="createName" class="grey-text">Name</label>
            <input class="form-control" type="text" id="createName" v-model="creationName"/>
            <br>
            <label for="environmentName" class="grey-text">Environment</label>
            <select class="browser-default custom-select" id="environmentName" v-model="creationEnvironment">
              <option value="" disabled selected>Select an environment</option>
              <option v-for="e in environmentNames" :key="e" :value="e">{{ e }}</option>
            </select>
            <br>
            <br>
            <br>
            <div v-if="createDTO.loadBalancer.type !== undefined && createDTO.loadBalancer.type !== ''">
              Load Balancer
              <br>
              <label for="loadBalancerTypeCreate" class="grey-text">Type</label>
              <select class="browser-default custom-select" id="loadBalancerTypeCreate" v-model="createDTO.loadBalancer.type">
                <option value="Select value" disabled selected>Select type</option>
                <option value="ROUND_ROBIN">Round Robin</option>
                <option value="LEAST_CONN">Least Connections</option>
                <option value="RANDOM">Random</option>
                <option value="CONSISTENT_HASH">Consistent Hash</option>
              </select>
              <br>
              <div v-if="createDTO.loadBalancer.type === 'CONSISTENT_HASH'">
                <br>
                <label for="minimumRingSizeCreate" class="grey-text">Minimum Ring Size</label>
                <input class="form-control" type="number" id="minimumRingSizeCreate"
                       v-model="createDTO.loadBalancer.consistentHash.minimumRingSize"/>
                <br>
                <select class="browser-default custom-select" id="consistentHashTypeCreate"
                        v-model="createDTO.loadBalancer.consistentHash.type">
                  <option value="" disabled selected>Select Consistent Hash Match Type</option>
                  <option value="header">Header Name</option>
                  <option value="queryParam">Query Parameter Name</option>
                  <option value="source">Source Ip</option>
                  <option value="cookie">Cookie</option>
                </select>
                <div v-if="createDTO.loadBalancer.consistentHash.type === 'header'">
                  <label for="headerNameCreate" class="grey-text">Header Name</label>
                  <input class="form-control" type="text" id="headerNameCreate"
                         v-model="createDTO.loadBalancer.consistentHash.match.headerName"/>
                </div>
                <div v-else-if="createDTO.loadBalancer.consistentHash.type === 'queryParam'">
                  <label for="queryParamCreate" class="grey-text">Query Parameter Name</label>
                  <input class="form-control" type="text" id="queryParamCreate"
                         v-model="createDTO.loadBalancer.consistentHash.match.queryParameterName"/>
                </div>
                <div v-else-if="createDTO.loadBalancer.consistentHash.type === 'source'">
                  <label for="sourceCreate" class="grey-text">Use Source IP?</label>
                  <input class="form-control" type="checkbox" id="sourceCreate"
                         v-model="createDTO.loadBalancer.consistentHash.match.useSourceIp"/>
                </div>
                <div v-else-if="createDTO.loadBalancer.consistentHash.type === 'cookie'">
                  <br>
                  <label for="cookieNameCreate" class="grey-text">Cookie Name</label>
                  <input class="form-control" type="text" id="cookieNameCreate"
                         v-model="createDTO.loadBalancer.consistentHash.match.cookie.name"/>
                  <br>
                  <label for="cookiePathCreate" class="grey-text">Cookie Path</label>
                  <input class="form-control" type="text" id="cookiePathCreate"
                         v-model="createDTO.loadBalancer.consistentHash.match.cookie.path"/>
                  <br>
                  <label for="cookieTtlCreate" class="grey-text">Cookie TTL</label>
                  <input class="form-control" type="text" id="cookieTtlCreate"
                         v-model="createDTO.loadBalancer.consistentHash.match.cookie.ttl"/>
                </div>
              </div>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.loadBalancer = {type: 'Select value', consistentHash: {minimumRingSize: 1024, type: '', match: {cookie: {}}}}">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Load Balancer
              </mdb-btn>
            </div>
            <br>
            <div v-if="createDTO.circuitBreaker.type !== ''">
              Circuit Breaker
              <br>
              <label for="gatewayErrorsCreate" class="grey-text">Consecutive Gateway Errors
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="consecutiveGatewayTooltip()"
                          class="mr-3"/>
              </label>
              <input class="form-control" type="number" id="gatewayErrorsCreate"
                     v-model="createDTO.circuitBreaker.consecutiveGatewayErrors"/>
              <br>
              <label for="5ErrorsCreate" class="grey-text">Consecutive 5XX Errors
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="consecutive5XXTooltip()" class="mr-3"/>
              </label>
              <input class="form-control" type="number" id="5ErrorsCreate"
                     v-model="createDTO.circuitBreaker.consecutive5xxErrors"/>
              <br>
              <label for="intervalCreate" class="grey-text">Interval Time
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="intervalTooltip()" class="mr-3"/>
              </label>
              <input class="form-control" type="text" id="intervalCreate" v-model="createDTO.circuitBreaker.interval"/>
              <br>
              <label for="baseEjectionCreate" class="grey-text">Base Ejection Time
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="baseEjectionTooltip()" class="mr-3"/>
              </label>
              <input class="form-control" type="text" id="baseEjectionCreate"
                     v-model="createDTO.circuitBreaker.baseEjectionTime"/>
              <br>
              <label for="maxEjCreate" class="grey-text">Max Ejection Percentage
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="maxEjectionTooltip()" class="mr-3"/>
              </label>
              <input class="form-control" type="number" id="maxEjCreate"
                     v-model="createDTO.circuitBreaker.maxEjectionPercent"/>
              <br>
              <label for="minHeCreate" class="grey-text">Min Health Percentage
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="minHealthTooltip()" class="mr-3"/>
              </label>
              <input class="form-control" type="number" id="minHeCreate" v-model="createDTO.circuitBreaker.minHealthPercent"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;" @click.native="createDTO.circuitBreaker = { type: 'not set'}">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Circuit Breaker
              </mdb-btn>
            </div>
          </mdb-modal-body>
          <mdb-modal-footer>
            <mdb-btn style="background-color: #00A082;" size="sm" @click.native="createModal = false, errors = ''">Close
            </mdb-btn>
            <mdb-btn color="primary" size="sm" v-bind:disabled="!formEnabled" type="submit">Create</mdb-btn>
            <br>
            <p class="font-weight-bold" style="color:red">{{ errors }}</p>
          </mdb-modal-footer>
        </form>
      </mdb-modal>
      <!-- EDIT MODAL -->
      <mdb-modal size="md" centered :show="editModal" @close="editModal = false, errors = ''">
        <mdb-modal-header>
          <mdb-modal-title>Edit <strong>{{ modalDTO.name.toUpperCase() }} - {{
              modalDTO.environment.toUpperCase()
            }}</strong></mdb-modal-title>
        </mdb-modal-header>
        <form v-on:submit.prevent="updateData">
          <mdb-modal-body>
            <div v-if="updateDTO.loadBalancer">
              Load Balancer
              <br>
              <label for="loadBalancerType" class="grey-text">Type</label>
              <select class="browser-default custom-select" id="loadBalancerType" v-model="updateDTO.loadBalancer.type">
                <option value="" selected>None</option>
                <option value="ROUND_ROBIN">Round Robin</option>
                <option value="LEAST_CONN">Least Connections</option>
                <option value="RANDOM">Random</option>
                <option value="CONSISTENT_HASH">Consistent Hash</option>
              </select>
              <br>
              <div v-if="updateDTO.loadBalancer.type === 'CONSISTENT_HASH'">
                <br>
                <label for="minimumRingSize" class="grey-text">Minimum Ring Size</label>
                <input class="form-control" type="number" id="minimumRingSize"
                       v-model="updateDTO.loadBalancer.consistentHash.minimumRingSize"/>
                <br>
                <select class="browser-default custom-select" id="consistentHashType"
                        v-model="updateDTO.loadBalancer.consistentHash.type">
                  <option value="" disabled selected>Select Consistent Hash Match Type</option>
                  <option value="header">Header Name</option>
                  <option value="queryParam">Query Parameter Name</option>
                  <option value="source">Source Ip</option>
                  <option value="cookie">Cookie</option>
                </select>
                <div v-if="updateDTO.loadBalancer.consistentHash.type === 'header'">
                  <label for="headerName" class="grey-text">Header Name</label>
                  <input class="form-control" type="text" id="headerName"
                         v-model="updateDTO.loadBalancer.consistentHash.match.headerName"/>
                </div>
                <div v-else-if="updateDTO.loadBalancer.consistentHash.type === 'queryParam'">
                  <label for="queryParam" class="grey-text">Query Parameter Name</label>
                  <input class="form-control" type="text" id="queryParam"
                         v-model="updateDTO.loadBalancer.consistentHash.match.queryParameterName"/>
                </div>
                <div v-else-if="updateDTO.loadBalancer.consistentHash.type === 'source'">
                  <label for="source" class="grey-text">Use Source IP?</label>
                  <input class="form-control" type="checkbox" id="source"
                         v-model="updateDTO.loadBalancer.consistentHash.match.useSourceIp"/>
                </div>
                <div v-else-if="updateDTO.loadBalancer.consistentHash.type === 'cookie'">
                  <br>
                  <label for="cookieName" class="grey-text">Cookie Name</label>
                  <input class="form-control" type="text" id="cookieName"
                         v-model="updateDTO.loadBalancer.consistentHash.match.cookie.name"/>
                  <br>
                  <label for="cookiePath" class="grey-text">Cookie Path</label>
                  <input class="form-control" type="text" id="cookiePath"
                         v-model="updateDTO.loadBalancer.consistentHash.match.cookie.path"/>
                  <br>
                  <label for="cookieTtl" class="grey-text">Cookie TTL</label>
                  <input class="form-control" type="text" id="cookieTtl"
                         v-model="updateDTO.loadBalancer.consistentHash.match.cookie.ttl"/>
                </div>
              </div>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.loadBalancer = {type: '', consistentHash: {minimumRingSize: 1024, type: '', match: {cookie: {}}}}">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Load Balancer
              </mdb-btn>
            </div>
            <br>
            <div v-if="updateDTO.circuitBreaker">
              Circuit Breaker
              <br>
              <label for="gatewayErrors" class="grey-text">Consecutive Gateway Errors
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="consecutiveGatewayTooltip()"
                          class="mr-3"/>
              </label>
              <input class="form-control" type="number" id="gatewayErrors"
                     v-model="updateDTO.circuitBreaker.consecutiveGatewayErrors"/>
              <br>
              <label for="5Errors" class="grey-text">Consecutive 5XX Errors
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="consecutive5XXTooltip()" class="mr-3"/>
              </label>
              <input class="form-control" type="number" id="5Errors"
                     v-model="updateDTO.circuitBreaker.consecutive5xxErrors"/>
              <br>
              <label for="interval" class="grey-text">Interval Time
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="intervalTooltip()" class="mr-3"/>
              </label>
              <input class="form-control" type="text" id="interval" v-model="updateDTO.circuitBreaker.interval"/>
              <br>
              <label for="baseEjection" class="grey-text">Base Ejection Time
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="baseEjectionTooltip()" class="mr-3"/>
              </label>
              <input class="form-control" type="text" id="baseEjection"
                     v-model="updateDTO.circuitBreaker.baseEjectionTime"/>
              <br>
              <label for="maxEj" class="grey-text">Max Ejection Percentage
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="maxEjectionTooltip()" class="mr-3"/>
              </label>
              <input class="form-control" type="number" id="maxEj"
                     v-model="updateDTO.circuitBreaker.maxEjectionPercent"/>
              <br>
              <label for="minHe" class="grey-text">Min Health Percentage
                <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="minHealthTooltip()" class="mr-3"/>
              </label>
              <input class="form-control" type="number" id="minHe" v-model="updateDTO.circuitBreaker.minHealthPercent"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;" @click.native="updateDTO.circuitBreaker = {}">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Circuit Breaker
              </mdb-btn>
            </div>
          </mdb-modal-body>
          <mdb-modal-footer>
            <mdb-btn style="background-color: #00A082;" size="sm" @click.native="editModal = false, errors = ''">Close
            </mdb-btn>
            <mdb-btn color="primary" size="sm" v-bind:disabled="!formEnabled" type="submit">Save changes</mdb-btn>
            <br>
            <p class="font-weight-bold" style="color:red">{{ errors }}</p>
          </mdb-modal-footer>
        </form>
      </mdb-modal>
      <!-- DELETE MODAL -->
      <mdb-modal frame position="bottom" direction="bottom" :show="deleteModal"
                 @close="deleteModal = false, errors = ''">
        <mdb-modal-body class="text-center">
          <span>Are you sure you want to delete <strong>{{
              modalDTO.name.toUpperCase()
            }} - {{ modalDTO.environment.toUpperCase() }}</strong></span>
          <mdb-btn style="background-color: #00A082;" @click.native="deleteModal = false, errors = ''">Close</mdb-btn>
          <mdb-btn v-on:click="deleteData" color="danger">CONFIRM DELETE</mdb-btn>
          <p class="font-weight-bold" style="color:red">{{ errors }}</p>
        </mdb-modal-body>
      </mdb-modal>
    </div>
  </div>
</template>

<script>
import backendHttpClient from "@/plugins/axios";

function getTrafficPolicyListFromResponse(response) {
  let trafficPolicyList = []

  response.data.trafficPolicies.forEach(entry => {
    trafficPolicyList.push(entry)
  })

  return trafficPolicyList;
}

function validateName(name) {
  if (name == null || name.length <= 3) {
    return 'Name has to be greater than 3 characters'
  } else if (!/^[a-z-]+$/.test(name)) {
    return 'Name cant contain spaces nor numbers. Only dash \'-\' are allowed.'
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
  name: "TrafficPolicyInfo",
  data() {
    return {
      trafficPolicies: [],
      createModal: false,
      editModal: false,
      deleteModal: false,
      modalDTO: {name: '', environment: ''},
      defaultDTO: {loadBalancer: {consistentHash: {minimumRingSize: 1024, type: '', match: {cookie: {}}}}, circuitBreaker: {type: ''}},
      updateDTO: {loadBalancer: {consistentHash: {minimumRingSize: 1024, type: '', match: {cookie: {}}}}},
      createDTO: {loadBalancer: {consistentHash: {minimumRingSize: 1024, type: '', match: {cookie: {}}}}, circuitBreaker: {}},
      creationName: '',
      creationEnvironment: '',
      environmentNames: [],
      pendingResponse: false,
      formEnabled: true,
      errors: '',
    }
  },
  created: function () {
    this.getData()
    this.getEnvironments()
  },
  methods: {
    isThereErrors() {
      return this.errors.length > 0
    },
    validateCreationData() {
      this.errors = validateName(this.creationName)
    },
    createData(){
      this.formEnabled = false
      this.createModal = false
      this.errors = ''
      this.validateCreationData()

      if (!this.isThereErrors()) {
        this.pendingResponse = true
        const serviceName = this.$route.params.microserviceName
        const envName = this.creationEnvironment
        const name = this.creationName

        const body = {
          loadBalancer: this.createDTO.loadBalancer,
          circuitBreaker: this.createDTO.circuitBreaker
        }

        backendHttpClient.post(serviceName + '/' + envName + '/networking/traffic-policy/' + name, body).then(() => {
          this.getData()
          this.formEnabled = true
          this.createModal = false
          this.pendingResponse = false
        })
        .catch(() => {
          this.errors = 'Error creating new traffic policy'
          this.formEnabled = true
          this.createModal = true
          this.pendingResponse = false
        })
      } else {
        this.formEnabled = true
        this.createModal = true
      }
    },
    validateUpdateData() {
      return ''
    },
    updateData() {
      this.formEnabled = false
      this.editModal = false
      this.errors = ''
      this.validateUpdateData()

      if (!this.isThereErrors()) {
        this.pendingResponse = true

        let loadBalancer = null
        if (this.updateDTO.loadBalancer.type !== '') {
          loadBalancer = this.updateDTO.loadBalancer
        }

        const body = {
          loadBalancer: loadBalancer,
          circuitBreaker: this.updateDTO.circuitBreaker
        }

        const serviceName = this.$route.params.microserviceName
        const envName = this.updateDTO.environment
        backendHttpClient.put(serviceName + '/' + envName + '/networking/traffic-policy/' + this.updateDTO.name,
            body).then(() => {
          this.getData()
          this.formEnabled = true
          this.editModal = false
          this.pendingResponse = false
        })
        .catch(() => {
          this.errors = 'Error updating traffic policy'
          this.formEnabled = true
          this.editModal = true
          this.pendingResponse = false
        })
      } else {
        this.formEnabled = true
        this.editModal = true
      }
    },
    deleteData() {
      this.deleteModal = false
      this.pendingResponse = true
      this.errors = ''

      const serviceName = this.$route.params.microserviceName
      const envName = this.modalDTO.environment
      backendHttpClient.delete(serviceName + '/' + envName + '/networking/traffic-policy/' + this.modalDTO.name).then(
          () => {
            this.getData()
            this.deleteModal = false
            this.pendingResponse = false
          })
      .catch(() => {
        this.errors = 'Error deleting traffic policy'
        this.deleteModal = true
        this.pendingResponse = false
      })
    },
    getData() {
      const serviceName = this.$route.params.microserviceName
      backendHttpClient.get(serviceName + '/networking').then(dataResponse => {
        this.trafficPolicies = getTrafficPolicyListFromResponse(dataResponse)
      })
    },
    getEnvironments() {
      const serviceName = this.$route.params.microserviceName
      backendHttpClient.get(serviceName + '/environment').then(environmentResponse => {
        this.environmentNames = getEnvironmentNamesFromResponse(environmentResponse)
      })
    },
    consecutiveGatewayTooltip() {
      return "Number of gateway errors before a host is ejected from the connection pool. When the upstream host is accessed over HTTP, a 502, 503, or 504 return code qualifies as a gateway error. Note that consecutivegatewayerrors and consecutive5xxerrors can be used separately or together. Because the errors counted by consecutivegatewayerrors are also included in consecutive5xxerrors, if the value of consecutivegatewayerrors is greater than or equal to the value of consecutive5xxerrors, consecutivegatewayerrors will have no effect."
    },
    consecutive5XXTooltip() {
      return "Number of 5xx errors before a host is ejected from the connection pool."
    },
    intervalTooltip() {
      return "Time interval between ejection sweep analysis. format: 1h/1m/1s/1ms. MUST BE >=1ms."
    },
    baseEjectionTooltip() {
      return "Minimum ejection duration. A host will remain ejected for a period equal to the product of minimum ejection duration and the number of times the host has been ejected. This technique allows the system to automatically increase the ejection period for unhealthy upstream servers. format: 1h/1m/1s/1ms. MUST BE >=1ms."
    },
    maxEjectionTooltip() {
      return "Maximum % of hosts in the load balancing pool for the upstream service that can be ejected."
    },
    minHealthTooltip() {
      return "Circuit breaker will be enabled as long as the associated load balancing pool has at least minhealthpercent hosts in healthy mode. When the percentage of healthy hosts in the load balancing pool drops below this threshold, circuit breaker will be disabled and the proxy will load balance across all hosts in the pool (healthy and unhealthy). The threshold can be disabled by setting it to 0%. "
    }
  }
}
</script>

<style scoped>

</style>