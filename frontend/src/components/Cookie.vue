<template>
    <div>
        <img v-bind:src="imagePath">
    </div>
</template>

<script>
    export default {
        name: "Cookie",
        props: {
            value: Number,
            error: Boolean
        },
        computed: {
            normalizedValue() {
                let nValue = Math.min(Math.max(this.value, 0), 100);
                nValue = Math.round(nValue / 10) * 10;
                return nValue;
            },

            imagePath() {
                if (this.error) { return `/cookie_icons/error.png`; }
                let nValue = this.normalizedValue;
                return `/cookie_icons/${nValue}.png`;
            }
        }
    }
</script>

<style lang="stylus" scoped>
img
    max-width: 100%;
    height: auto;
</style>