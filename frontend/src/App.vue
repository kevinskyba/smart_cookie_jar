<template>
  <div id="app">
    <router-view/>
  </div>
</template>
<style lang="stylus">
    @import "theme/variables.styl";
    body
        font-family: $primary-font-family;
        background-color: $background-color;
        color: $primary-font-color;
</style>
