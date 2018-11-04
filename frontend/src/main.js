import Vue from 'vue'
import App from './App.vue'
import router from './router'

import makeMockAPIService from './services/mockapi.service';
import makeChromeService from "./services/chrome.service";
import makeDataService from "./services/data.service";

Vue.config.productionTip = false;
document.addEventListener('DOMContentLoaded', function() {
    const apiService = makeMockAPIService();
    const chromeService = makeChromeService();
    const dataService = makeDataService(chromeService, apiService);

    new Vue({
        router,
        apiService,
        chromeService,
        dataService,
        render: h => h(App)
    }).$mount('#app')
});
