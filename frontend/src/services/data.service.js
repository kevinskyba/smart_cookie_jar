import Vue from 'vue';
import Rx from 'rx';

class DataService {
    constructor(chromeService, apiService) {
        this.chromeService = chromeService;
        this.apiService = apiService;

        this.data = new Rx.Subject();
        this.error = new Rx.Subject();

        Rx.Observable.timer(0, 10000).subscribe(() => {
            this.apiService.getStatus().then((status) => {
                this.data.onNext(status);
            }).catch((err) => {
                console.error(err);
                this.error.onNext(err);
            });
        });
    }
}

Vue.mixin({
    beforeCreate() {
        const options = this.$options;
        if (options.dataService) {
            this.$dataService = options.dataService;
        } else if (options.parent && options.parent.$dataService) {
            this.$dataService = options.parent.$dataService;
        }
    }
});

export default function makeDataService(chromeService, apiService) {
    return new DataService(chromeService, apiService);
}