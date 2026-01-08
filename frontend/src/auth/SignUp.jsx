import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function SignUp() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [usernameError, setUsernameError] = useState("");
  const [emailError, setEmailError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [strengthLabel, setStrengthLabel] = useState("");
  const [strengthColor, setStrengthColor] = useState("#6b7280");
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
    const hasError = validateAll();
    if (hasError) return;
    setError("");
    setSubmitting(true);
    // Simulate async submit. Replace with real API call.
    setTimeout(() => {
      setSubmitting(false);
      try {
        const raw = localStorage.getItem("dbp_users");
        const users = raw ? JSON.parse(raw) : [];
        // avoid duplicates by email or username
        const exists = users.some(u => u.email?.toLowerCase() === email.trim().toLowerCase() || u.username?.toLowerCase() === username.trim().toLowerCase());
        if (!exists) {
          users.push({
            username: username.trim(),
            email: email.trim(),
            password: password, // for demo only; never store plaintext in production
            createdAt: new Date().toISOString()
          });
          localStorage.setItem("dbp_users", JSON.stringify(users));
        }
      } catch (_) { /* noop for demo */ }
      navigate("/login");
    }, 1200);
  }

  function validateAll() {
    let any = false;
    if (!username.trim()) {
      setUsernameError("Username is required");
      any = true;
    } else {
      setUsernameError("");
    }

    if (!email.trim()) {
      setEmailError("Email is required");
      any = true;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      setEmailError("Enter a valid email address");
      any = true;
    } else {
      setEmailError("");
    }

    if (!password.trim()) {
      setPasswordError("Password is required");
      any = true;
    } else if (password.length < 8) {
      setPasswordError("Password must be at least 8 characters");
      any = true;
    } else {
      setPasswordError("");
    }
    if (any) setError("Please fill the fields below");
    return any;
  }

  function computeStrength(pw) {
    if (!pw) {
      setStrengthLabel("");
      setStrengthColor("#6b7280");
      return;
    }
    let score = 0;
    if (pw.length >= 8) score += 1;
    if (/[A-Z]/.test(pw)) score += 1;
    if (/[0-9]/.test(pw)) score += 1;
    if (/[^A-Za-z0-9]/.test(pw)) score += 1;
    if (score <= 1) { setStrengthLabel("Weak"); setStrengthColor("#b91c1c"); }
    else if (score === 2) { setStrengthLabel("Medium"); setStrengthColor("#d97706"); }
    else { setStrengthLabel("Strong"); setStrengthColor("#15803d"); }
  }

  return (
    <div className="auth-layout" style={{ height: "100vh", overflow: "hidden" }}>
      {/* Right signup area */}
      <div className="right-wrap">
        <div className="right-card">
          <h2 className="card-title">Create your account</h2>
          <p className="card-sub">Sign up to get started</p>

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
              <label htmlFor="username" className="field-label">Username</label>
              <input
                id="username"
                type="text"
                className="input"
                value={username}
                onChange={(e) => {
                  setUsername(e.target.value);
                  if (usernameError) setUsernameError("");
                }}
                onBlur={() => {
                  if (!username.trim()) setUsernameError("Username is required");
                }}
                placeholder="johndoe"
              />
              {usernameError ? (
                <div style={{ color: "#b91c1c", fontSize: 12, marginTop: 4 }}>{usernameError}</div>
              ) : null}

              <label htmlFor="email" className="field-label">Email</label>
              <div className="input-wrap">
                <span className="input-icon" aria-hidden>
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden>
                    <path d="M3 7a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2v10a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7Z" stroke="#94a3b8" strokeWidth="1.5"/>
                    <path d="m4 7 8 6 8-6" stroke="#94a3b8" strokeWidth="1.5"/>
                  </svg>
                </span>
                <input
                  id="email"
                  type="email"
                  className="input"
                  value={email}
                  onChange={(e) => {
                    setEmail(e.target.value);
                    if (emailError) setEmailError("");
                  }}
                  onBlur={() => {
                    if (!email.trim()) setEmailError("Email is required");
                    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) setEmailError("Enter a valid email address");
                  }}
                  placeholder="you@example.com"
                />
              {emailError ? (
                <div style={{ color: "#b91c1c", fontSize: 12, marginTop: 4 }}>{emailError}</div>
              ) : null}
              </div>

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
                  type={showPassword ? "text" : "password"}
                  className="input"
                  value={password}
                  onChange={(e) => {
                    setPassword(e.target.value);
                    if (passwordError) setPasswordError("");
                    computeStrength(e.target.value);
                  }}
                  onBlur={() => {
                    if (!password.trim()) setPasswordError("Password is required");
                    else if (password.length < 8) setPasswordError("Password must be at least 8 characters");
                  }}
                  placeholder="Enter your password"
                  style={{ paddingRight: 84 }}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword((s) => !s)}
                  aria-label={showPassword ? "Hide password" : "Show password"}
                  title={showPassword ? "Hide password" : "Show password"}
                  style={{
                    position: "absolute",
                    right: 12,
                    top: 6,
                    height: 28,
                    border: "none",
                    background: "transparent",
                    color: "#64748b",
                    fontWeight: 600,
                    cursor: "pointer"
                  }}
                >
                  {showPassword ? (
                    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden>
                      <path d="M1 1l22 22" stroke="#64748b" strokeWidth="2" strokeLinecap="round"/>
                      <path d="M17.94 17.94A10.94 10.94 0 0 1 12 20c-7 0-11-8-11-8a21.77 21.77 0 0 1 5.06-6.94" stroke="#64748b" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                      <path d="M9.88 5.09A10.94 10.94 0 0 1 12 4c7 0 11 8 11 8a21.77 21.77 0 0 1-4.87 6.14" stroke="#64748b" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                      <path d="M10.58 10.58A3 3 0 0 0 15 15" stroke="#64748b" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                    </svg>
                  ) : (
                    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden>
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8S1 12 1 12z" stroke="#64748b" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                      <circle cx="12" cy="12" r="3" stroke="#64748b" strokeWidth="2"/>
                    </svg>
                  )}
                </button>
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", fontSize: 12, marginTop: 4 }}>
                <span style={{ color: passwordError ? "#b91c1c" : "#6b7280" }}>Password must be 8+ characters</span>
                {password ? (
                  <span style={{ color: strengthColor, fontWeight: 600 }}>{strengthLabel}</span>
                ) : null}
              </div>
              </div>

              <button
                type="submit"
                className="signin-btn"
                disabled={submitting}
                style={{ opacity: submitting ? 0.7 : 1, cursor: submitting ? "not-allowed" : "pointer" }}
                onClick={() => { validateAll(); }}
              >
                {submitting ? "Loading..." : "Sign up"}
              </button>

              {/* or separator */}
              <div style={{ display: "flex", alignItems: "center", gap: 8, margin: "12px 0" }}>
                <span style={{ flex: 1, height: 1, background: "#e5e7eb" }} />
                <span style={{ color: "#6b7280", fontSize: 12 }}>or</span>
                <span style={{ flex: 1, height: 1, background: "#e5e7eb" }} />
              </div>

              {/* Continue with Google (moved below) */}
              <button type="button" aria-label="Continue with Google" style={{
                width: "100%",
                background: "#fff",
                color: "#0f172a",
                border: "1px solid #e5e7eb",
                padding: "10px 12px",
                borderRadius: 9999,
                cursor: "pointer",
                fontWeight: 700,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: 8,
              }}
                onClick={() => {
                  // hook up to Google auth flow here
                }}
              >
                <svg width="18" height="18" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden>
                  <path fill="#EA4335" d="M12 10.2v3.9h5.4c-.2 1.2-1.6 3.6-5.4 3.6-3.2 0-5.8-2.6-5.8-5.8S8.8 6.1 12 6.1c1.8 0 3 .8 3.7 1.5l2.5-2.5C16.8 3.7 14.6 2.8 12 2.8 6.9 2.8 2.8 6.9 2.8 12s4.1 9.2 9.2 9.2c5.3 0 8.8-3.7 8.8-8.9 0-.6-.1-1-.1-1.5H12z"/>
                </svg>
                Continue with Google
              </button>

              <div className="note">
                Already have an account? {""}
                <Link to="/login">Sign in</Link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
