<template>
  <div id="app">
    <mdb-navbar id="nav-bar" style="background-color: #F2CC38;" dark>
      <mdb-navbar-brand href="https://mdbootstrap.com/">
        <router-link id="name" style="color: white;" to="/">Microservices Infra</router-link>
      </mdb-navbar-brand>
    </mdb-navbar>
    <router-view/>
  </div>
</template>

<style lang="scss">
@import url('https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap');

$image-path: '~@/../mdb/mdbvue/img';
@import '~@/../mdb/mdbvue/scss/mdb-free.scss';

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}

#nav-bar {
  z-index: 1055;
}
</style>
