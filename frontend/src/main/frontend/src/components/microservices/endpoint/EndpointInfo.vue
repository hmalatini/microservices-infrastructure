<template>
  <div>
    <div style="position: fixed; top: 50%;" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else>
      <div style="margin: 20px 0">
        <mdb-btn size="lg" style="background-color: #00A082 !important;"
                 @click.native="createModal = true, createDTO = Object.assign({}, defaultDTO)">
          Create New Endpoint
        </mdb-btn>
      </div>
      <div v-if="endpoints.length > 0">
        <mdb-tbl hover>
          <mdb-tbl-head>
            <tr>
              <th>Name</th>
              <th>Environment</th>
              <th>Type</th>
              <th>Traffic Policy</th>
            </tr>
          </mdb-tbl-head>
          <mdb-tbl-body>
            <tr v-for="(entity, index) in endpoints" :key="index">
              <td>{{ entity.name }}</td>
              <td>{{ entity.environment }}</td>
              <td>{{ entity.type }}</td>
              <td v-if="entity.trafficPolicy.name">
                {{ entity.trafficPolicy.name }}
              </td>
              <td v-else>
                <mdb-icon icon="times" class="mr-3"/>
              </td>
              <td>
                <mdb-btn size="sm" color="primary"
                         @click.native="editModal = true, modalDTO = entity, updateDTO = getUpdateDTO(Object.assign({}, entity))">
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
          <mdb-modal-title>Create new Endpoint</mdb-modal-title>
        </mdb-modal-header>
        <form v-on:submit.prevent="createData">
          <mdb-modal-body>
            <label for="createName" class="grey-text">Name <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointNameTooltip()" class="mr-3"/></label>
            <input class="form-control" type="text" id="createName" v-model="creationName"/>
            <br>
            <label for="environmentName" class="grey-text">Environment</label>
            <select class="browser-default custom-select" id="environmentName" v-model="creationEnvironment">
              <option value="" disabled selected>Select an environment</option>
              <option v-for="(e, index) in environmentNames" :key="index" :value="e">{{ e }}</option>
            </select>
            <br>
            <br>
            <label for="endpointType" class="grey-text">Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointTypeTooltip()" class="mr-3"/></label>
            <select class="browser-default custom-select" id="endpointType" v-model="creationType">
              <option value="" disabled selected>Select an endpoint type</option>
              <option v-for="(e, index) in endpointTypes" :key="index" :value="e">{{ e }}</option>
            </select>
            <br>
            <br>
            <label for="trafficPolicyCreation" class="grey-text">Traffic Policy</label>
            <select class="browser-default custom-select" id="trafficPolicyCreation" v-model="createDTO.trafficPolicy">
              <option value="" selected>None</option>
              <option v-for="(e, index) in trafficPolicies" :key="index" :value="e.name">{{ e.name }}</option>
            </select>
            <br>
            <br>
            <hr>
            MATCH
            <br>
            <br>
            URI Match
            <br>
            <div v-if="createDTO.match.uri.type !== ''">
              <label for="uriMatchTypeCreation" class="grey-text">URI Match Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="matchTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="uriMatchTypeCreation"
                      v-model="createDTO.match.uri.type">
                <option value="" disabled selected>Select a match type</option>
                <option v-for="(e, index) in matchTypes" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <label for="uriMatchValueCreation" class="grey-text">URI Match Value</label>
              <input class="form-control" type="text" id="uriMatchValueCreation" v-model="createDTO.match.uri.value"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.match.uri.type = {type: 'Select value'}">
                <mdb-icon icon="plus" class="mr-1"/>
                Add URI Match
              </mdb-btn>
            </div>
            <br>
            <br>
            Method Match
            <br>
            <div v-if="createDTO.match.method.type !== ''">
              <label for="methodMatchTypeCreation" class="grey-text">Method Match Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="matchTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="methodMatchTypeCreation"
                      v-model="createDTO.match.method.type">
                <option value="" disabled selected>Select a match type</option>
                <option v-for="(e, index) in matchTypes" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <br>
              <label for="methodMatchValueCreation" class="grey-text">Method Match Value</label>
              <input class="form-control" type="text" id="methodMatchValueCreation"
                     v-model="createDTO.match.method.value"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.match.method.type = {type: 'Select value'}">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Method Match
              </mdb-btn>
            </div>
            <br>
            <br>
            Header Match
            <br>
            <div v-for="(header, index) in createDTO.match.headers" :key="index + 'headermatch'">
              <label for="headerMatchNameCreation" class="grey-text">Header Name</label>
              <input class="form-control" type="text" id="headerMatchNameCreation" v-model="header.name"/>
              <br>
              <label for="headerMatchTypeCreation" class="grey-text">Header Value Match Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="matchTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="headerMatchTypeCreation" v-model="header.type">
                <option value="" disabled selected>Select a match type</option>
                <option v-for="(e, index) in matchTypes" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <label for="headerMatchValueCreation" class="grey-text">Header Match Value</label>
              <input class="form-control" type="text" id="headerMatchValueCreation" v-model="header.value"/>
              <br>
            </div>
            <div>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.match.headers.push({})">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Header Match
              </mdb-btn>
            </div>
            <br>
            <br>
            Not Containing Header Match
            <br>
            <div v-for="(noheader, index) in createDTO.match.noHeaders" :key="index + 'noheadermatch'">
              <label for="noheaderMatchNameCreation" class="grey-text">Header Name</label>
              <input class="form-control" type="text" id="noheaderMatchNameCreation" v-model="noheader.name"/>
              <br>
              <label for="noheaderMatchTypeCreation" class="grey-text">Header Value Match Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="matchTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="noheaderMatchTypeCreation" v-model="noheader.type">
                <option value="" disabled selected>Select a match type</option>
                <option v-for="(e, index) in matchTypes" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <label for="noheaderMatchValueCreation" class="grey-text">Header Match Value</label>
              <input class="form-control" type="text" id="noheaderMatchValueCreation" v-model="noheader.value"/>
              <br>
            </div>
            <div>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.match.noHeaders.push({name: '', type: '', value: ''})">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Header Match
              </mdb-btn>
            </div>
            <br>
            <br>
            Query Param Match
            <br>
            <div v-for="(queryParam, index) in createDTO.match.queryParams" :key="index + 'queryparammatch'">
              <label for="queryParamMatchNameCreation" class="grey-text">Query Param Name</label>
              <input class="form-control" type="text" id="queryParamMatchNameCreation" v-model="queryParam.name"/>
              <br>
              <label for="queryParamMatchTypeCreation" class="grey-text">Query Param Value Match Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="matchTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="queryParamMatchTypeCreation" v-model="queryParam.type">
                <option value="" disabled selected>Select a match type</option>
                <option v-for="(e, index) in matchTypes" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <label for="queryParamMatchValueCreation" class="grey-text">Query Param Match Value</label>
              <input class="form-control" type="text" id="queryParamMatchValueCreation" v-model="queryParam.value"/>
              <br>
            </div>
            <div>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.match.queryParams.push({})">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Header Match
              </mdb-btn>
            </div>
            <br>
            <br>
            <hr>
            ENDPOINT CONFIGS
            <br>
            <br>
            Rewrite Endpoint
            <br>
            <div v-if="createDTO.rewrite !== ' '">
              <label for="rewriteCreation" class="grey-text">Rewrite URI  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointRewriteTooltip()" class="mr-3"/></label>
              <input class="form-control" type="text" id="rewriteCreation" v-model="createDTO.rewrite"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.rewrite = ''">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Rewrite Config
              </mdb-btn>
            </div>
            <br>
            <br>
            Redirect Endpoint
            <br>
            <div v-if="createDTO.redirect !== ' '">
              <label for="redirectCreation" class="grey-text">Redirect To URI <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointRedirectTooltip()" class="mr-3"/></label>
              <input class="form-control" type="text" id="redirectCreation" v-model="createDTO.redirect"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.redirect = ''">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Redirect Config
              </mdb-btn>
            </div>
            <br>
            <br>
            Timeout
            <br>
            <div v-if="createDTO.timeout !== ' '">
              <label for="timeoutCreation" class="grey-text">Timeout <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointTimeoutTooltip()" class="mr-3"/></label>
              <input class="form-control" type="text" id="timeoutCreation" v-model="createDTO.timeout"/>
              <br>
              <br>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.timeout = ''">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Timeout Config
              </mdb-btn>
            </div>
            <hr>
            RETRIES
            <br>
            <div v-if="createDTO.retries.type !== undefined">
              <br>
              <br>
              <label for="retriesAttemptsCreation" class="grey-text">Attempts <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointRetriesAttemptsTooltip()" class="mr-3"/></label>
              <input class="form-control" type="number" id="retriesAttemptsCreation"
                     v-model="createDTO.retries.attempts"/>
              <br>
              <br>
              <label for="retriesTimeoutCreation" class="grey-text">Timeout per try <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointRetriesTimeoutTooltip()" class="mr-3"/></label>
              <input class="form-control" type="text" id="retriesTimeoutCreation"
                     v-model="createDTO.retries.perTryTimeout"/>
              <br>
              <br>
              <label for="retriesOnCreation" class="grey-text">Retry On <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointRetriesOnTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="retriesOnCreation" v-model="createDTO.retries.retryOn">
                <option value="" disabled selected>Select a retries on</option>
                <option v-for="(e, index) in this.retryOn" :key="index" :value="e">{{ e }}</option>
              </select>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.retries = {type: 'Select Value'}">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Retries Config
              </mdb-btn>
            </div>
            <br>
            <br>
            <hr>
            FAULT INJECTION
            <br>
            <br>
            Delay
            <br>
            <div v-if="createDTO.faults.delay.type !== undefined">
              <label for="delayTimeCreation" class="grey-text">Delay Time <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointDelayTimeTooltip()" class="mr-3"/></label>
              <input class="form-control" type="text" id="delayTimeCreation"
                     v-model="createDTO.faults.delay.fixedDelay"/>
              <br>
              <br>
              <label for="delayPercentageCreation" class="grey-text">Delay Percentage <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointFaultPercentageTooltip()" class="mr-3"/></label>
              <input class="form-control" type="number" id="delayPercentageCreation"
                     v-model="createDTO.faults.delay.percentage"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.faults.delay = {type: 'Select Value'}">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Delay Config
              </mdb-btn>
            </div>
            <br>
            <br>
            Abort
            <br>
            <div v-if="createDTO.faults.abort.type !== undefined">
              <label for="abortCreation" class="grey-text">Abort Status Code <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointAbortStatusCodeTooltip()" class="mr-3"/></label>
              <input class="form-control" type="number" id="abortCreation" v-model="createDTO.faults.abort.httpStatus"/>
              <br>
              <br>
              <label for="abortPercentageCreation" class="grey-text">Abort Percentage <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointFaultPercentageTooltip()" class="mr-3"/></label>
              <input class="form-control" type="number" id="abortPercentageCreation"
                     v-model="createDTO.faults.abort.percentage"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.faults.abort = {type: 'Select Value'}">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Abort Config
              </mdb-btn>
            </div>
            <br>
            <br>
            <hr>
            HEADER OPERATIONS
            <br>
            <br>
            Request
            <br>
            <div v-for="(header, index) in createDTO.headers.request" :key="index + 'requestheader'">
              <label for="requestHeaderOperationTypeCreation" class="grey-text">Operation Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointHeaderOperationTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="requestHeaderOperationTypeCreation"
                      v-model="header.operation">
                <option value="" disabled selected>Select an operation type</option>
                <option v-for="(e, index) in headerOperationType" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <label for="requestHeaderNameCreation" class="grey-text">Header Name</label>
              <input class="form-control" type="text" id="requestHeaderNameCreation" v-model="header.headerName"/>
              <br>
              <div v-if="header.operation !== 'REMOVE'">
                <label for="requestHeaderValueCreation" class="grey-text">Header Value</label>
                <input class="form-control" type="text" id="requestHeaderValueCreation" v-model="header.headerValue"/>
                <br>
              </div>
            </div>
            <div>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.headers.request.push({})">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Request Header Operation
              </mdb-btn>
            </div>
            <br>
            <br>
            Response
            <br>
            <div v-for="(header, index) in createDTO.headers.response" :key="index + 'responseheader'">
              <label for="responseHeaderOperationTypeCreation" class="grey-text">Operation Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointHeaderOperationTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="responseHeaderOperationTypeCreation"
                      v-model="header.operation">
                <option value="" disabled selected>Select an operation type</option>
                <option v-for="(e, index) in headerOperationType" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <label for="responseHeaderNameCreation" class="grey-text">Header Name</label>
              <input class="form-control" type="text" id="responseHeaderNameCreation" v-model="header.headerName"/>
              <br>
              <div v-if="header.operation !== 'REMOVE'">
                <label for="responseHeaderValueCreation" class="grey-text">Header Value</label>
                <input class="form-control" type="text" id="responseHeaderValueCreation" v-model="header.headerValue"/>
                <br>
              </div>
            </div>
            <div>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="createDTO.headers.response.push({})">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Response Header Operation
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
            <label for="trafficPolicyUpdate" class="grey-text">Traffic Policy</label>
            <select class="browser-default custom-select" id="trafficPolicyUpdate" v-model="updateDTO.trafficPolicy">
              <option value="" selected>None</option>
              <option v-for="(e, index) in trafficPolicies" :key="index" :value="e">{{ e.name }}</option>
            </select>
            <br>
            <br>
            <hr>
            MATCH
            <br>
            <br>
            URI Match
            <br>
            <div v-if="updateDTO.match.uri.type !== null">
            <label for="uriMatchTypeUpdate" class="grey-text">URI Match Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="matchTypeTooltip()" class="mr-3"/></label>
            <select class="browser-default custom-select" id="uriMatchTypeUpdate" v-model="updateDTO.match.uri.type">
              <option value="" disabled selected>Select a match type</option>
              <option v-for="(e, index) in matchTypes" :key="index" :value="e.toUpperCase()">{{ e }}</option>
            </select>
            <br>
            <label for="uriMatchValueUpdate" class="grey-text">URI Match Value</label>
            <input class="form-control" type="text" id="uriMatchValueUpdate" v-model="updateDTO.match.uri.value"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.match.uri = {type: ''}">
                <mdb-icon icon="plus" class="mr-1"/>
                Add URI Match
              </mdb-btn>
            </div>
            <br>
            <br>
            Method Match
            <br>
            <div v-if="updateDTO.match.method.type !== null">
            <label for="methodMatchTypeUpdate" class="grey-text">Method Match Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="matchTypeTooltip()" class="mr-3"/></label>
            <select class="browser-default custom-select" id="methodMatchTypeUpdate"
                    v-model="updateDTO.match.method.type">
              <option value="" disabled selected>Select a match type</option>
              <option v-for="(e, index) in matchTypes" :key="index" :value="e.toUpperCase()">{{ e }}</option>
            </select>
            <br>
            <br>
            <label for="methodMatchValueUpdate" class="grey-text">Method Match Value</label>
            <input class="form-control" type="text" id="methodMatchValueUpdate"
                   v-model="updateDTO.match.method.value"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.match.method = {type: ''}">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Method Match
              </mdb-btn>
            </div>
            <br>
            <br>
            Header Match
            <br>
            <div v-for="(header, index) in updateDTO.match.headers" :key="index + 'headermatch'">
              <label for="headerMatchNameUpdate" class="grey-text">Header Name</label>
              <input class="form-control" type="text" id="headerMatchNameUpdate" v-model="header.name"/>
              <br>
              <label for="headerMatchTypeUpdate" class="grey-text">Header Value Match Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="matchTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="headerMatchTypeUpdate" v-model="header.type">
                <option value="" disabled selected>Select a match type</option>
                <option v-for="(e, index) in matchTypes" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <label for="headerMatchValueUpdate" class="grey-text">Header Match Value</label>
              <input class="form-control" type="text" id="headerMatchValueUpdate" v-model="header.value"/>
              <br>
            </div>
            <div>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.match.headers.push({})">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Header Match
              </mdb-btn>
            </div>
            <br>
            <br>
            Not Containing Header Match
            <br>
            <div v-for="(noheader, index) in updateDTO.match.noHeaders" :key="index + 'noheadermatch'">
              <label for="noheaderMatchNameUpdate" class="grey-text">Header Name</label>
              <input class="form-control" type="text" id="noheaderMatchNameUpdate" v-model="noheader.name"/>
              <br>
              <label for="noheaderMatchTypeUpdate" class="grey-text">Header Value Match Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="matchTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="noheaderMatchTypeUpdate" v-model="noheader.type">
                <option value="" disabled selected>Select a match type</option>
                <option v-for="(e, index) in matchTypes" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <label for="noheaderMatchValueUpdate" class="grey-text">Header Match Value</label>
              <input class="form-control" type="text" id="noheaderMatchValueUpdate" v-model="noheader.value"/>
              <br>
            </div>
            <div>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.match.noHeaders.push({name: '', type: '', value: ''})">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Header Match
              </mdb-btn>
            </div>
            <br>
            <br>
            Query Param Match
            <br>
            <div v-for="(queryParam, index) in updateDTO.match.queryParams" :key="index + 'queryparammatch'">
              <label for="queryParamMatchNameUpdate" class="grey-text">Query Param Name</label>
              <input class="form-control" type="text" id="queryParamMatchNameUpdate" v-model="queryParam.name"/>
              <br>
              <label for="queryParamMatchTypeUpdate" class="grey-text">Query Param Value Match Type <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="matchTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="queryParamMatchTypeUpdate" v-model="queryParam.type">
                <option value="" disabled selected>Select a match type</option>
                <option v-for="(e, index) in matchTypes" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <label for="queryParamMatchValueUpdate" class="grey-text">Query Param Match Value</label>
              <input class="form-control" type="text" id="queryParamMatchValueUpdate" v-model="queryParam.value"/>
              <br>
            </div>
            <div>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.match.queryParams.push({})">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Header Match
              </mdb-btn>
            </div>
            <br>
            <br>
            <hr>
            ENDPOINT CONFIGS
            <br>
            <br>
            Rewrite Endpoint
            <br>
            <div v-if="updateDTO.rewrite !== null">
            <label for="rewriteUpdate" class="grey-text">Rewrite URI  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointRewriteTooltip()" class="mr-3"/></label>
            <input class="form-control" type="text" id="rewriteUpdate" v-model="updateDTO.rewrite"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.rewrite = ''">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Rewrite Config
              </mdb-btn>
            </div>
            <br>
            <br>
            Redirect Endpoint
            <br>
            <div v-if="updateDTO.redirect !== null">
            <label for="redirectUpdate" class="grey-text">Redirect To URI  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointRedirectTooltip()" class="mr-3"/></label>
            <input class="form-control" type="text" id="redirectUpdate" v-model="updateDTO.redirect"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.redirect = ''">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Redirect Config
              </mdb-btn>
            </div>
            <br>
            <br>
            Timeout
            <br>
            <div v-if="updateDTO.timeout !== null">
            <label for="timeoutUpdate" class="grey-text">Timeout  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointTimeoutTooltip()" class="mr-3"/></label>
            <input class="form-control" type="text" id="timeoutUpdate" v-model="updateDTO.timeout"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.timeout = ''">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Timeout Config
              </mdb-btn>
            </div>
            <br>
            <br>
            <hr>
            RETRIES
            <br>
            <div v-if="updateDTO.retries.perTryTimeout !== null">
            <br>
            <br>
            <label for="retriesAttemptsUpdate" class="grey-text">Attempts  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointRetriesAttemptsTooltip()" class="mr-3"/></label>
            <input class="form-control" type="number" id="retriesAttemptsUpdate"
                   v-model="updateDTO.retries.attempts"/>
            <br>
            <br>
            <label for="retriesTimeoutUpdate" class="grey-text">Timeout per try  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointRetriesTimeoutTooltip()" class="mr-3"/></label>
            <input class="form-control" type="text" id="retriesTimeoutUpdate"
                   v-model="updateDTO.retries.perTryTimeout"/>
            <br>
            <br>
            <label for="retriesOnUpdate" class="grey-text">Retry On  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointRetriesOnTooltip()" class="mr-3"/></label>
            <select class="browser-default custom-select" id="retriesOnUpdate" v-model="updateDTO.retries.retryOn">
              <option value="" disabled selected>Select a retries on</option>
              <option v-for="(e, index) in this.retryOn" :key="index" :value="e">{{ e }}</option>
            </select>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.retries.perTryTimeout = ''">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Retries Config
              </mdb-btn>
            </div>
            <br>
            <br>
            <hr>
            FAULT INJECTION
            <br>
            <br>
            Delay
            <br>
            <div v-if="updateDTO.faults.delay.fixedDelay !== null">
            <br>
            <br>
            <label for="delayTimeUpdate" class="grey-text">Delay Time  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointDelayTimeTooltip()" class="mr-3"/></label>
            <input class="form-control" type="text" id="delayTimeUpdate" v-model="updateDTO.faults.delay.fixedDelay"/>
            <br>
            <br>
            <label for="delayPercentageUpdate" class="grey-text">Delay Percentage  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointFaultPercentageTooltip()" class="mr-3"/></label>
            <input class="form-control" type="number" id="delayPercentageUpdate"
                   v-model="updateDTO.faults.delay.percentage"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.faults.delay.fixedDelay = ''">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Delay Config
              </mdb-btn>
            </div>
            <br>
            <br>
            Abort
            <br>
            <div v-if="updateDTO.faults.abort.httpStatus !== null">
            <br>
            <br>
            <label for="abortUpdate" class="grey-text">Abort Status Code  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointAbortStatusCodeTooltip()" class="mr-3"/></label>
            <input class="form-control" type="number" id="abortUpdate" v-model="updateDTO.faults.abort.httpStatus"/>
            <br>
            <br>
            <label for="abortPercentageUpdate" class="grey-text">Abort Percentage  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointFaultPercentageTooltip()" class="mr-3"/></label>
            <input class="form-control" type="number" id="abortPercentageUpdate"
                   v-model="updateDTO.faults.abort.percentage"/>
            </div>
            <div v-else>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.faults.abort.httpStatus = ''">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Abort Config
              </mdb-btn>
            </div>
            <br>
            <br>
            <hr>
            HEADER OPERATIONS
            <br>
            <br>
            Request
            <br>
            <div v-for="(header, index) in updateDTO.headers.request" :key="index + 'requestheader'">
              <label for="requestHeaderOperationTypeUpdate" class="grey-text">Operation Type  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointHeaderOperationTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="requestHeaderOperationTypeUpdate"
                      v-model="header.operation">
                <option value="" disabled selected>Select an operation type</option>
                <option v-for="(e, index) in headerOperationType" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <label for="requestHeaderNameUpdate" class="grey-text">Header Name</label>
              <input class="form-control" type="text" id="requestHeaderNameUpdate" v-model="header.headerName"/>
              <br>
              <div v-if="header.operation !== 'REMOVE'">
                <label for="requestHeaderValueUpdate" class="grey-text">Header Value</label>
                <input class="form-control" type="text" id="requestHeaderValueUpdate" v-model="header.headerValue"/>
                <br>
              </div>
            </div>
            <div>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.headers.request.push({})">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Request Header Operation
              </mdb-btn>
            </div>
            <br>
            <br>
            Response
            <br>
            <div v-for="(header, index) in updateDTO.headers.response" :key="index + 'responseheader'">
              <label for="responseHeaderOperationTypeUpdate" class="grey-text">Operation Type  <mdb-icon icon="info-circle" data-mdb-toggle="tooltip" :title="endpointHeaderOperationTypeTooltip()" class="mr-3"/></label>
              <select class="browser-default custom-select" id="responseHeaderOperationTypeUpdate"
                      v-model="header.operation">
                <option value="" disabled selected>Select an operation type</option>
                <option v-for="(e, index) in headerOperationType" :key="index" :value="e.toUpperCase()">{{ e }}</option>
              </select>
              <br>
              <label for="responseHeaderNameUpdate" class="grey-text">Header Name</label>
              <input class="form-control" type="text" id="responseHeaderNameUpdate" v-model="header.headerName"/>
              <br>
              <div v-if="header.operation !== 'REMOVE'">
                <label for="responseHeaderValueUpdate" class="grey-text">Header Value</label>
                <input class="form-control" type="text" id="responseHeaderValueUpdate" v-model="header.headerValue"/>
                <br>
              </div>
            </div>
            <div>
              <mdb-btn style="background-color: #0eb2c4 !important;"
                       @click.native="updateDTO.headers.response.push({})">
                <mdb-icon icon="plus" class="mr-1"/>
                Add Response Header Operation
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
            }} - {{ modalDTO.environment.toUpperCase() }} - {{ modalDTO.type.toUpperCase() }}</strong></span>
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

function getEndpointListFromResponse(response) {
  let endpointList = []

  response.data.publicEndpoints.forEach(entry => {
    entry.type = 'public'
    endpointList.push(entry)
  })

  response.data.privateEndpoints.forEach(entry => {
    entry.type = 'private'
    endpointList.push(entry)
  })

  return endpointList;
}

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

function getMatchMapFromArray(array) {
  let map = {}
  if (array === undefined || Object.entries(array).length === 0 || array.length === 0) {
    return map
  }

  for (let entry of array) {
    if (entry.name !== undefined && entry.name !== '' && entry.value !== undefined && entry.value !== '' && entry.type
        !== undefined && entry.type !== '') {
      map[entry.name] = {type: entry.type, value: entry.value}
    }
  }

  return map
}

function getMatch(match) {
  if (match === undefined || match.type === undefined || match.type === '' || match.value === undefined || match.value
      === '') {
    return undefined
  }

  return match
}

function getRetries(retries) {
  if (retries !== undefined && retries !== null && retries.retryOn !== '') {
    return retries
  }

  return {}
}

export default {
  name: "EndpointInfo",
  data() {
    return {
      endpoints: [],
      trafficPolicies: [],
      createModal: false,
      editModal: false,
      deleteModal: false,
      modalDTO: {name: '', environment: '', type: ''},
      defaultDTO: {
        trafficPolicy: '',
        match: {uri: {type: ''}, method: {type: ''}, headers: [], noHeaders: [], queryParams: []},
        rewrite: ' ',
        redirect: ' ',
        timeout: ' ',
        retries: {retryOn: ''},
        faults: {
          delay: {},
          abort: {}
        },
        headers: {
          request: [],
          response: []
        }
      },
      updateDTO: {
        match: {uri: {}, method: {}, headers: [], noHeaders: [], queryParams: []}, retries: {},
        rewrite: '',
        redirect: '',
        timeout: '',
        faults: {
          delay: {},
          abort: {}
        },
        headers: {
          request: [],
          response: []
        }
      },
      createDTO: {
        match: {uri: {}, method: {}, headers: [], noHeaders: [], queryParams: []}, retries: {},
        rewrite: '',
        redirect: '',
        timeout: '',
        faults: {
          delay: {},
          abort: {}
        },
        headers: {
          request: [],
          response: []
        }
      },
      creationName: '',
      creationEnvironment: '',
      creationType: '',
      environmentNames: [],
      matchTypes: ['exact', 'prefix', 'regex'],
      endpointTypes: ['public', 'private'],
      retryOn: ['FIVE_XX', 'GATEWAY_ERROR', 'RESET', 'CONNECT_FAILURE', 'RETRIABLE_STATUS_CODES',
        'RETRIABLE_HEADERS'],
      headerOperationType: ['add', 'set', 'remove'],
      pendingResponse: false,
      formEnabled: true,
      errors: '',
    }
  },
  created: function () {
    this.getData()
    this.getEnvironments()
  }
  ,
  methods: {
    isThereErrors() {
      return this.errors.length > 0
    },
    validateCreationData() {
      this.errors = validateName(this.creationName)
    },
    createData() {
      this.formEnabled = false
      this.createModal = false
      this.errors = ''
      this.validateCreationData()

      if (!this.isThereErrors()) {
        this.pendingResponse = true
        const serviceName = this.$route.params.microserviceName
        const envName = this.creationEnvironment
        const name = this.creationName
        const type = this.creationType

        const body = {
          match: {
            uri: getMatch(this.createDTO.match.uri),
            method: getMatch(this.createDTO.match.method),
            headers: getMatchMapFromArray(this.createDTO.match.headers),
            noHeaders: getMatchMapFromArray(this.createDTO.match.noHeaders),
            queryParams: getMatchMapFromArray(this.createDTO.match.queryParams),
          },
          rewrite: this.createDTO.rewrite,
          redirect: this.createDTO.redirect,
          timeout: this.createDTO.timeout,
          retries: getRetries(this.createDTO.retries),
          faults: this.createDTO.faults,
          headers: this.createDTO.headers
        }

        console.log(body)

        let trafficPolicy = ''
        if (this.createDTO.trafficPolicy !== undefined && this.createDTO.trafficPolicy !== '') {
          trafficPolicy = '?trafficPolicyName=' + this.createDTO.trafficPolicy
        }

        backendHttpClient.post(
            serviceName + '/' + envName + '/networking/endpoint/' + name + '/' + type + trafficPolicy, body).then(
            () => {
              this.getData()
              this.formEnabled = true
              this.createModal = false
              this.pendingResponse = false
            })
        .catch(() => {
          this.errors = 'Error creating new endpoint'
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

        const body = {
          match: {
            uri: getMatch(this.updateDTO.match.uri),
            method: getMatch(this.updateDTO.match.method),
            headers: getMatchMapFromArray(this.updateDTO.match.headers),
            noHeaders: getMatchMapFromArray(this.updateDTO.match.noHeaders),
            queryParams: getMatchMapFromArray(this.updateDTO.match.queryParams),
          },
          rewrite: this.updateDTO.rewrite,
          redirect: this.updateDTO.redirect,
          timeout: this.updateDTO.timeout,
          retries: this.updateDTO.retries,
          faults: this.updateDTO.faults,
          headers: this.updateDTO.headers
        }

        let trafficPolicy = ''
        if (this.updateDTO.trafficPolicy !== undefined && this.updateDTO.trafficPolicy !== '') {
          trafficPolicy = '?trafficPolicyName=' + this.updateDTO.trafficPolicy.name
        }

        const serviceName = this.$route.params.microserviceName
        const envName = this.updateDTO.environment
        backendHttpClient.put(
            serviceName + '/' + envName + '/networking/endpoint/' + this.updateDTO.name + '/' + this.updateDTO.type
            + trafficPolicy,
            body).then(() => {
          this.getData()
          this.formEnabled = true
          this.editModal = false
          this.pendingResponse = false
        })
        .catch(() => {
          this.errors = 'Error updating endpoint'
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
      backendHttpClient.delete(
          serviceName + '/' + envName + '/networking/endpoint/' + this.modalDTO.name + '/' + this.modalDTO.type).then(
          () => {
            this.getData()
            this.deleteModal = false
            this.pendingResponse = false
          })
      .catch(() => {
        this.errors = 'Error deleting endpoint'
        this.deleteModal = true
        this.pendingResponse = false
      })
    },
    getData() {
      const serviceName = this.$route.params.microserviceName
      backendHttpClient.get(serviceName + '/networking').then(dataResponse => {
        this.endpoints = getEndpointListFromResponse(dataResponse)
        this.trafficPolicies = getTrafficPolicyListFromResponse(dataResponse)
      })
    },
    getEnvironments() {
      const serviceName = this.$route.params.microserviceName
      backendHttpClient.get(serviceName + '/environment').then(environmentResponse => {
        this.environmentNames = getEnvironmentNamesFromResponse(environmentResponse)
      })
    },
    getUpdateDTO: function (copy) {
      if (copy === undefined || copy.match === undefined) {
        return copy
      }

      if (Object.entries(copy.match.headers).length > 0) {
        const arr = []
        for (let h in copy.match.headers) {
          let current = {}
          current.name = h.toString()
          current.type = copy.match.headers[h].type
          current.value = copy.match.headers[h].value
          arr.push(current)
        }
        copy.match.headers = arr
      } else {
        copy.match.headers = []
      }

      if (Object.entries(copy.match.noHeaders).length > 0) {
        const arr = []
        for (let h in copy.match.noHeaders) {
          let current = {}
          current.name = h.toString()
          current.type = copy.match.noHeaders[h].type
          current.value = copy.match.noHeaders[h].value
          arr.push(current)
        }
        copy.match.noHeaders = arr
      } else {
        copy.match.noHeaders = []
      }

      if (Object.entries(copy.match.queryParams).length > 0) {
        const arr = []
        for (let h in copy.match.queryParams) {
          let current = {}
          current.name = h.toString()
          current.type = copy.match.queryParams[h].type
          current.value = copy.match.queryParams[h].value
          arr.push(current)
        }
        copy.match.queryParams = arr
      } else {
        copy.match.queryParams = []
      }

      if (copy.headers.request === undefined || copy.headers.request === null || copy.headers.request.length === 0) {
        copy.headers.request = []
      }

      if (copy.headers.response === undefined || copy.headers.response === null || copy.headers.response.length === 0) {
        copy.headers.response = []
      }

      if (copy.trafficPolicy.name === null) {
        copy.trafficPolicy = ''
      }

      return copy
    },
    endpointNameTooltip() {
      return "Name without spaces nor special characters. The only special character is dash '-'"
    },
    endpointTypeTooltip() {
      return "Public: it is going to receive traffic coming from public network (outside of the cluster)\nPrivate: it is going to receive traffic coming from private network (inside of the cluster), and it is used when you use as host the service name.This means no risk of collision with other service endpoint"
    },
    matchTypeTooltip() {
      return "exact: exact string match\n"
          + "prefix: prefix-based match\n"
          + "regex: RE2 style regex-based match (https://github.com/google/re2/wiki/Syntax)."
    },
    endpointRewriteTooltip() {
      return "Can be used to rewrite specific parts of a HTTP request before forwarding the request to the destination. Rewrite cannot be used with Redirect"
    },
    endpointRedirectTooltip() {
      return "Can be used to send a 301 redirect response to the caller, where the Authority/Host and the URI in the response can be swapped with the specified values. Redirect cannot be used with Rewrite"
    },
    endpointTimeoutTooltip() {
      return "Timeout for HTTP requests, default is disabled. format: 1h/1m/1s/1ms. MUST BE >=1ms."
    },
    endpointRetriesAttemptsTooltip() {
      return "Number of retries to be allowed for a given request."
    },
    endpointRetriesTimeoutTooltip() {
      return "Timeout per attempt for a given request, including the initial call and any retries. Format: 1h/1m/1s/1ms. MUST BE >=1ms."
    },
    endpointRetriesOnTooltip() {
      return "Specifies the conditions under which retry takes place.\n" +
          "FIVE_XX: Will attempt a retry if the upstream server responds with any 5xx response code, or does not respond at all (disconnect/reset/read timeout). (Includes connect-failure and refused-stream)\n" +
          "GATEWAY_ERROR: This policy is similar to the 5xx policy but will only retry requests that result in a 502, 503, or 504.\n" +
          "RESET: Will attempt a retry if the upstream server does not respond at all (disconnect/reset/read timeout.)\n" +
          "CONNECT_FAILURE: Envoy will attempt a retry if a request is failed because of a connection failure to the upstream server (connect timeout, etc.). (Included in 5xx). NOTE: A connection failure/timeout is a the TCP level, not the request level.\n" +
          "RETRIABLE_STATUS_CODES: Will attempt a retry if the upstream server responds with any response code matching one defined in the x-envoy-retriable-status-codes header. The list is a comma delimited list of integers: “409” would cause 409 to be considered retriable, while “504,409” would consider both 504 and 409 retriable.\n" +
          "RETRIABLE_HEADERS: Will attempt a retry if the upstream server response includes any headers matching in the x-envoy-retriable-header-names header. The list is a comma-separated list of header names: “X-Upstream-Retry,X-Try-Again” would cause any upstream responses containing either one of the specified headers to be retriable. Header names are case-insensitive.\n"
    },
    endpointDelayTimeTooltip() {
      return "Add a fixed delay before forwarding the request. Format: 1h/1m/1s/1ms. MUST be >=1ms."
    },
    endpointFaultPercentageTooltip() {
      return "Percentage of requests on which the fault will be injected."
    },
    endpointAbortStatusCodeTooltip() {
      return "HTTP status code to use to abort the Http request."
    },
    endpointHeaderOperationTypeTooltip() {
      return "set: Overwrite the headers specified by key with the given values\n"
          + "add: Append the given values to the headers specified by keys (will create a comma-separated list of values)\n"
          + "remove: Remove a the specified headers"
    }
  }
}
</script>

<style scoped>

</style>