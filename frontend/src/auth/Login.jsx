import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function Login() {
  const navigate = useNavigate();
  const [identifier, setIdentifier] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("customer"); // 'customer' | 'admin'
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const prevHtmlOverflow = document.documentElement.style.overflow;
    const prevBodyOverflow = document.body.style.overflow;
    document.documentElement.style.overflow = "hidden";
    document.body.style.overflow = "hidden";
    return () => {
      document.documentElement.style.overflow = prevHtmlOverflow;
      document.body.style.overflow = prevBodyOverflow;
    };
  }, []);

  function handleSubmit(e) {
    e.preventDefault();
    if (!identifier.trim() || !password.trim()) {
      setError("Email or username and password required");
      return;
    }
    // Check localStorage for registered users (from SignUp)
    try {
      const raw = localStorage.getItem("dbp_users");
      const users = raw ? JSON.parse(raw) : [];
      const idLower = identifier.trim().toLowerCase();
      const user = users.find(
        (u) => u.email?.toLowerCase() === idLower || u.username?.toLowerCase() === idLower
      );
      if (!user || user.password !== password) {
        setError("Invalid credentials or account not found. Please sign up first.");
        return;
      }
    } catch (_) {
      setError("Unable to verify account locally. Please try again.");
      return;
    }

    // Store a simple session for Dashboard greeting
    try {
      const raw = localStorage.getItem("dbp_users");
      const users = raw ? JSON.parse(raw) : [];
      const idLower = identifier.trim().toLowerCase();
      const user = users.find(
        (u) => u.email?.toLowerCase() === idLower || u.username?.toLowerCase() === idLower
      );
      if (user) {
        localStorage.setItem(
          "dbp_session",
          JSON.stringify({ username: user.username || "", email: user.email || "" })
        );
      }
    } catch (_) { /* noop */ }

    setError("");
    setSubmitting(true);
    // Simulate async login. Replace with real API call.
    setTimeout(() => {
      setSubmitting(false);
      navigate("/dashboard");
    }, 800);
  }

  return (
    <div className="auth-layout" style={{ height: "100vh", overflow: "hidden" }}>
      {/* Right login area */}
      <div className="right-wrap">
        <div className="right-card">
          <h2 className="card-title">Welcome back</h2>
          <p className="card-sub">Sign in to access your account</p>

          {/* Role tabs */}
          <div className="tabs">
            <button type="button" className={`tab ${role === "customer" ? "active" : ""}`} onClick={() => setRole("customer")}>Customer</button>
            <button type="button" className={`tab ${role === "admin" ? "active" : ""}`} onClick={() => setRole("admin")}>Admin</button>
          </div>

          <div className="form-inner">
          {error ? (
            <div role="alert" aria-live="polite" style={{
              background: "#fef2f2",
              color: "#991b1b",
              border: "1px solid #fecaca",
              padding: 10,
              borderRadius: 8,
              marginBottom: 12,
              fontSize: 14
            }}>
              {error}
            </div>
          ) : null}

          <form onSubmit={handleSubmit} noValidate>
            {/* Email */}
            <label htmlFor="identifier" className="field-label">Email or Username</label>
            <div className="input-wrap">
              <span className="input-icon" aria-hidden>
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden>
                  <path d="M3 7a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2v10a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7Z" stroke="#94a3b8" strokeWidth="1.5"/>
                  <path d="m4 7 8 6 8-6" stroke="#94a3b8" strokeWidth="1.5"/>
                </svg>
              </span>
              <input
                id="identifier"
                type="text"
                className="input"
                value={identifier}
                onChange={(e) => setIdentifier(e.target.value)}
                placeholder="Enter email or username"
              />
            </div>

            {/* Password */}
            <label htmlFor="password" className="field-label">Password</label>
            <div className="input-wrap" style={{ marginBottom: 16 }}>
              <span className="input-icon" aria-hidden>
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden>
                  <rect x="6" y="10" width="12" height="10" rx="2" stroke="#94a3b8" strokeWidth="1.5"/>
                  <path d="M8 10V7a4 4 0 0 1 8 0v3" stroke="#94a3b8" strokeWidth="1.5"/>
                </svg>
              </span>
              <input
                id="password"
                type="password"
                className="input"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter your password"
              />
            </div>

            {/* Sign in */}
            <button
              type="submit"
              className="signin-btn"
              disabled={submitting}
              style={{ opacity: submitting ? 0.7 : 1, cursor: submitting ? "not-allowed" : "pointer" }}
              onClick={() => {
                if (!identifier.trim() || !password.trim()) {
                  setError("Email or username and password required");
                }
              }}
            >
              {submitting ? "Loading..." : "Sign In"}
            </button>

            {/* Subtle signup link */}
            <div className="note">
              Don’t have an account? {""}
              <Link to="/signup">
                Sign up
              </Link>
            </div>
          </form>

          </div>
        </div>
      </div>
    </div>
  );
}
