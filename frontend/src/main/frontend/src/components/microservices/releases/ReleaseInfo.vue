<template>
  <div>
    <div style="position: fixed; top: 50%;" v-if="pendingResponse" class="spinner-border text-warning" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div v-else>
      <div style="margin: 20px 0">
        <router-link to="releases/create">
          <mdb-btn style="background-color: #00A082;">Create New Release</mdb-btn>
        </router-link>
      </div>
      <div v-if="releases.length > 0">
        <mdb-tbl hover>
          <mdb-tbl-head>
            <tr>
              <th>Name</th>
            </tr>
          </mdb-tbl-head>
          <mdb-tbl-body>
            <tr v-for="release in releases" :key="release">
              <td>{{ release }}</td>
            </tr>
          </mdb-tbl-body>
        </mdb-tbl>
      </div>
    </div>
  </div>
</template>

<script>
import backendHttpClient from "@/plugins/axios";

function getReleaseListFromResponse(releaseResponse) {
  let releaseList = []

  releaseResponse.data.releaseVersionNames.forEach(release => {
    releaseList.push(release)
  })

  console.log(releaseList)

  return releaseList;
}

export default {
  name: "ReleaseInfo",
  data() {
    return {
      releases: [],
      pendingResponse: false,
    }
  },
  created: function () {
    this.getReleases()
  },
  methods: {
    getReleases() {
      const serviceName = this.$route.params.microserviceName
      backendHttpClient.get(serviceName + '/releases').then(releaseResponse => {
        this.releases = getReleaseListFromResponse(releaseResponse)
      })
    }
  }
}
</script>

<style scoped>

</style>