import axios from "axios";
import Vue from "vue";

class APIService {

    constructor() {

    }

    getStatus() {
        return new Promise((resolve, reject) => {
            axios({ method: "GET", url: process.env.VUE_APP_BACKEND_URL + '/lastobject'}).then(res => {
                if (res.data && res.data.fillLevel !== undefined) {
                    resolve(res.data.fillLevel);
                } else {
                    reject("Something is wrong in the data: " + JSON.stringify(res.data));
                }
            }).catch(err => {
                reject(err);
            });
        });
    }
}

Vue.mixin({
    beforeCreate() {
        const options = this.$options;
        if (options.apiService) {
            this.$apiService = options.apiService;
        } else if (options.parent && options.parent.$apiService) {
            this.$apiService = options.parent.$apiService;
        }
    }
});

export default function makeAPIService() {
    return new APIService();
}