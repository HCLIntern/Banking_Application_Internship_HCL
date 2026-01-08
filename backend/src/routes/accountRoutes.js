const express = require("express");
const router = express.Router();
router.get("/", (req, res) => res.json({message: "GET /accounts placeholder"}));
router.post("/", (req, res) => res.json({message: "POST /accounts placeholder"}));
module.exports = router;
