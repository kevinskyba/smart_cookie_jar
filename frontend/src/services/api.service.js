import axios from "axios";
import Vue from "vue";

class APIService {

    constructor() {

    }

    getStatus() {
        return new Promise((resolve, reject) => {
            axios({ method: "GET", url: process.env.VUE_APP_BACKEND_URL + '/v1/cookie-jar/state'}).then(res => {
                if (res["data"] !== undefined && res["data"]["percentage"] !== undefined) {
                    resolve(res["data"]["percentage"]);
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