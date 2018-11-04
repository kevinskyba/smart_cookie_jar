const url = "localhost";

function fetchBackground() {
    fetch(url).then((res) => {
        res.json().then((data) => {
            let percentage = data.fillLevel;
            if (percentage >= 0 && percentage <= 100) {
                chrome.storage.local.set({'fillLevel': percentage});
                const icon = Math.round(percentage / 10) * 10;
                changeIcon(icon);
            } else {
                changeIcon(0);
            }
        });
    }).catch((err) => {
        changeIcon(0);
        console.error(err);
    })
}

function changeIcon(size) {
    chrome.browserAction.setIcon({
        path: `cookie_icons/64/${size}.png`
    });
}

chrome.runtime.onInstalled.addListener(function() {
    setInterval(fetchBackground, 5000);
    fetchBackground();
});