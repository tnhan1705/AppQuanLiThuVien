const LOG_TYPE = Object.freeze({
    DATABASE: "~ DATABASE: ",
    NOTIFY: "~ NOTIFY: ",
    ERROR: "~ ERROR: ",
});

const EVENT = Object.freeze({
    LOGIN: "login",

})

// Export the enum for use in other scripts
module.exports = { LOG_TYPE, EVENT };