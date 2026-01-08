import React, { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function PaymentSuccess() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const tx = state || {};

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

  const fmt = (v) => v ?? "—";

  return (
    <div style={styles.screen}>
      <main style={styles.container}>
        <section style={styles.card}>
          {/* Success indicator */}
          <div style={styles.iconWrap} aria-hidden>
            <div style={styles.iconCircle}>
              <svg width="36" height="36" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M20 6L9 17l-5-5" stroke="#10b981" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </div>
          </div>

          {/* Heading & message */}
          <h1 style={styles.title}>Payment Successful</h1>
          <p style={styles.subtitle}>Your transaction has been completed successfully.</p>

          {/* Summary */}
          <div style={styles.summary}>
            <div style={styles.row}><span style={styles.label}>Transaction ID</span><span style={styles.value}>{fmt(tx.id)}</span></div>
            <div style={styles.row}><span style={styles.label}>Amount</span><span style={styles.value}>{fmt(tx.amount)}</span></div>
            <div style={styles.row}><span style={styles.label}>Receiver</span><span style={styles.value}>{fmt(tx.receiver)}</span></div>
            <div style={styles.row}><span style={styles.label}>Method</span><span style={styles.value}>{fmt(tx.method)}</span></div>
            <div style={styles.row}><span style={styles.label}>Date & Time</span><span style={styles.value}>{fmt(tx.when)}</span></div>
          </div>

          <div style={styles.actions}>
            <button style={styles.primaryBtn} onClick={() => navigate("/dashboard")}>Go to Dashboard</button>
          </div>
        </section>
      </main>
    </div>
  );
}

const styles = {
  screen: {
    position: "fixed",
    inset: 0,
    width: "100vw",
    height: "100vh",
    padding: 16,
    boxSizing: "border-box",
    color: "#e5e7eb",
    overflow: "hidden",
  },
  container: {
    height: "100%",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    margin: "0 auto",
  },
  card: {
    width: "100%",
    maxWidth: 560,
    padding: 24,
    borderRadius: 16,
    background: "rgba(255,255,255,0.08)",
    border: "1px solid rgba(255,255,255,0.12)",
    boxShadow: "0 20px 50px rgba(0,0,0,0.45)",
    backdropFilter: "blur(10px)",
    WebkitBackdropFilter: "blur(10px)",
    textAlign: "center",
  },
  iconWrap: { display: "flex", justifyContent: "center", marginBottom: 8 },
  iconCircle: {
    height: 56,
    width: 56,
    borderRadius: 9999,
    display: "grid",
    placeItems: "center",
    background: "rgba(16,185,129,0.15)",
    border: "1px solid rgba(16,185,129,0.35)",
    boxShadow: "0 8px 24px rgba(16,185,129,0.25)",
  },
  title: { margin: "10px 0 4px", color: "#fff", fontSize: 26, fontWeight: 800 },
  subtitle: { margin: 0, color: "#cbd5e1", fontSize: 14 },
  summary: {
    textAlign: "left",
    marginTop: 16,
    padding: 12,
    borderRadius: 12,
    background: "rgba(0,0,0,0.25)",
    border: "1px solid rgba(255,255,255,0.08)",
  },
  row: { display: "flex", justifyContent: "space-between", padding: "8px 6px" },
  label: { color: "#94a3b8", fontSize: 13 },
  value: { color: "#fff", fontWeight: 700 },
  actions: { display: "flex", gap: 10, justifyContent: "center", marginTop: 16 },
  primaryBtn: {
    padding: "10px 14px",
    borderRadius: 9999,
    border: "1px solid rgba(255,255,255,0.18)",
    background:
      "radial-gradient(120% 120% at 0% 0%, rgba(255,255,255,0.15), rgba(255,255,255,0) 40%), linear-gradient(90deg, #7c3aed, #ec4899)",
    color: "#fff",
    fontWeight: 800,
    cursor: "pointer",
    boxShadow: "0 10px 30px rgba(236,72,153,0.35), 0 6px 16px rgba(124,58,237,0.3)",
  },
  ghostBtn: {
    padding: "10px 14px",
    borderRadius: 9999,
    background: "rgba(255,255,255,0.08)",
    border: "1px solid rgba(255,255,255,0.18)",
    color: "#e5e7eb",
    cursor: "pointer",
    fontWeight: 700,
  },
};
