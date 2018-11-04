import axios from "axios";
import Vue from "vue";


class APIService {

    constructor() {

    }

    getStatus() {
        return new Promise((resolve, reject) => {
            axios({ method: "GET", url: "..."}).then(res => {
                if (res.data && res.data.status) {
                    resolve(res.data.status);
                } else {
                    reject();
                }
            }, err => {
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