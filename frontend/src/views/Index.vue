<template>
    <div class="index">
        <h1 class="header-text">Smart Cookie Jar</h1>
        <div class="status-text">Derzeit scheint die Dose zu <b>{{status}}%</b> mit Keksen befüllt zu sein.</div>
        <cookie class="cookie" v-bind:value="status"></cookie>
        <span class="legal-corner">Alle Angaben ohne Gewähr.<br>Der Rechtsweg ist ausgeschlossen.</span>
        <footer>Created with VueJS, Spring, Arduino and <img src="/cookie_icons/100.png">.</footer>
    </div>
</template>

<script>
    import Cookie from "../components/Cookie";

    export default {
        name: "Index",
        components: {Cookie},
        data: function() {
            return {
                status: 0
            }
        },
        created() {
            this.$dataService.data.subscribe((status) => {
                this.status = status;
            });
        }
    }
</script>

<style lang="stylus" scoped>
    @import "../theme/variables.styl";
    .index
        max-width: $max-width;
        margin: 0 auto;
    .header-text
        font-size: $header-font-size;
        text-align: center;
    .cookie
        max-width: $cookie-width;
        margin: 0 auto;
    .status-text
        font-family: $regular-font-family;
        font-size: $primary-font-size;
        text-align: center;
    .legal-corner
        position: absolute;
        left: 5px;
        bottom: 5px;
        font-family: $regular-font-family;
        font-size: $small-font-size;
    footer
        font-family: $mono-font-family;
        font-size: $secondary-font-size;
        text-align: center;
        img
            height: $secondary-font-size;
            width: $secondary-font-size;

    @media $media-modal
        .index
            max-width: $modal-max-width;
        .header-text
            font-size: $modal-header-font-size;
        .status-text
            font-size: $modal-primary-font-size;
        .legal-corner
            font-size: $modal-small-font-size;
        footer
            font-size: $modal-secondary-font-size;
            img
                height: $modal-secondary-font-size;
                width: $modal-secondary-font-size;
</style>