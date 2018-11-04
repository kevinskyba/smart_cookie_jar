import Vue from 'vue';
import Rx from 'rx';

const chrome = chrome || {};

class ChromeService {
    constructor() {
        this.isChromeExtension = process.env.VUE_APP_MODE === "chrome";
        if (this.isChromeExtension) {
            this.doInitialStuff();
        }
    }

    doInitialStuff() {
        document.querySelector("html").style.width = "400px";
        document.querySelector("html").style.height = "550px";

        this.initialFillLevel = Rx.Observable.create((obs) => {
            chrome.storage.local.get('fillLevel', (data) => {
                obs.next(data);
                obs.complete();
            });
        });
    }
}

Vue.mixin({
    beforeCreate() {
        const options = this.$options;
        if (options.chromeService) {
            this.$chromeService = options.chromeService;
        } else if (options.parent && options.parent.$chromeService) {
            this.$chromeService = options.parent.$chromeService;
        }
    }
});

export default function makeChromeService() {
    return new ChromeService();
}