const express = require("express");
const router = express.Router();
router.get("/customers", (req, res) => res.json({message: "GET /admin/customers placeholder"}));
module.exports = router;
