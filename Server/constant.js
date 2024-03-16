const LOG_TYPE = Object.freeze({
    DATABASE: "~ DATABASE: ",
    NOTIFY: "~ NOTIFY: ",
    ERROR: "~ ERROR: ",
});

const EVENT = Object.freeze({
    LOGIN: "login",
    GET_DATA: "getData",
    ORDER: "order"
})

// Export the enum for use in other scripts
module.exports = { LOG_TYPE, EVENT };