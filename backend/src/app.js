const express = require("express");
const app = express();

app.use(express.json());

// routes
app.use("/auth", require("./routes/authRoutes"));
app.use("/accounts", require("./routes/accountRoutes"));
app.use("/admin", require("./routes/adminRoutes"));

// simple health check
app.get("/health", (req, res) => res.json({status: "ok"}));

module.exports = app;
