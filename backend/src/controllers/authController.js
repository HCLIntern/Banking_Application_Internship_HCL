exports.login = (req, res) => {
  // VERY simple placeholder: do not use in production.
  return res.json({
    message: "Login API working",
    token: "sample-jwt-token"
  });
};
