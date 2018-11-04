import Vue from "vue";

class MockAPIService {

    _status = 100;
    _interval = 5000;

    constructor() {
        setInterval(() => {
            this._status = this._status - 10;
            if (this._status < 0) {
                this._status = 100;
            }
        }, this._interval);
    }

    getStatus() {
        return new Promise((resolve) => {
            resolve(this._status);
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

export default function makeMockAPIService() {
    return new MockAPIService();
}