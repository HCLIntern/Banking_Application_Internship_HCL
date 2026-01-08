import React, { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const navigate = useNavigate();
  const [displayName, setDisplayName] = useState("User");
  const [recent, setRecent] = useState([]);
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
  useEffect(() => {
    try {
      const raw = localStorage.getItem("dbp_session");
      const s = raw ? JSON.parse(raw) : null;
      const name = (s?.username && s.username.trim()) || (s?.email && s.email.split("@")[0]) || "User";
      setDisplayName(name.charAt(0).toUpperCase() + name.slice(1));
    } catch (_) {}
  }, []);
  useEffect(() => {
    try {
      const raw = localStorage.getItem("dbp_txns");
      const list = raw ? JSON.parse(raw) : [];
      const sorted = Array.isArray(list) ? [...list].sort((a,b) => (b.whenTs||0) - (a.whenTs||0)) : [];
      setRecent(sorted.slice(0,3));
    } catch (_) {
      setRecent([]);
    }
  }, []);
  return (
    <div style={styles.screen}>
      {/* Top bar */}
      <header style={styles.topbar}>
        <div style={styles.brandRow}>
          <div style={styles.avatar} aria-hidden />
          <div style={styles.brandName}>Welcome, {displayName}</div>
        </div>
        <div style={styles.actions}>
          <button style={styles.ghostBtn} onClick={() => navigate("/payments")}>Payments</button>
          <button style={styles.ghostBtn} onClick={() => navigate("/payments/history", { state: { fromDashboard: true } })}>History</button>
          <button
            style={styles.ghostBtn}
            onClick={() => {
              try { localStorage.removeItem("dbp_session"); } catch (_) {}
              navigate("/login", { replace: true });
            }}
          >
            Log Out
          </button>
        </div>
      </header>

      {/* Main content card */}
      <main style={styles.container}>
        <section style={styles.block}>
          <div style={styles.blockHeader}>
            <div>
              <h2 style={styles.h2}>Account Overview</h2>
              <div style={styles.balance}>$0,00.00</div>
            </div>
            <button style={styles.primaryBtn} onClick={() => navigate("/payments")}>Make a Payment</button>
          </div>
          <div style={styles.hr} />
          <h3 style={styles.h3}>Recent Transactions</h3>
          <div style={styles.grid}>
            {(recent.length ? recent : []).map((t) => (
              <div key={t.id} style={styles.txn}>
                <div style={styles.txnTitle}>{t.receiver}</div>
                <div style={styles.txnAmt}>{t.amount}</div>
                <div style={styles.txnDate}>{t.when}</div>
              </div>
            ))}
            {recent.length === 0 ? (
              <div style={{ gridColumn: '1/-1', color: '#94a3b8', fontSize: 14 }}>No recent transactions</div>
            ) : null}
          </div>
          <div style={{ display: "flex", justifyContent: "flex-end", marginTop: 12 }}>
            <button style={styles.ghostBtn} onClick={() => navigate("/payments/history", { state: { fromDashboard: true } })}>Show More</button>
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
    overscrollBehavior: "none",
  },
  topbar: {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    padding: "10px 14px",
    borderRadius: 12,
    background: "rgba(255,255,255,0.06)",
    border: "1px solid rgba(255,255,255,0.12)",
    boxShadow: "0 10px 30px rgba(0,0,0,0.25)",
    marginBottom: 16,
    backdropFilter: "blur(8px)",
    WebkitBackdropFilter: "blur(8px)",
  },
  brandRow: { display: "flex", alignItems: "center", gap: 10 },
  avatar: {
    width: 32,
    height: 32,
    borderRadius: 9999,
    background: "linear-gradient(135deg, #7c3aed, #ec4899)",
    boxShadow: "0 4px 14px rgba(124,58,237,0.45)",
  },
  brandName: { fontWeight: 800 },
  actions: { display: "flex", alignItems: "center", gap: 10 },
  ghostBtn: {
    padding: "8px 12px",
    borderRadius: 9999,
    background: "rgba(255,255,255,0.08)",
    border: "1px solid rgba(255,255,255,0.18)",
    color: "#e5e7eb",
    cursor: "pointer",
    fontWeight: 700,
  },
  container: {
    maxWidth: 4000,
    margin: "0 auto",
  },
  block: {
    padding: 18,
    borderRadius: 16,
    background: "rgba(255,255,255,0.08)",
    border: "1px solid rgba(255,255,255,0.12)",
    boxShadow: "0 20px 50px rgba(0,0,0,0.45)",
    backdropFilter: "blur(10px)",
    WebkitBackdropFilter: "blur(10px)",
  },
  blockHeader: {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    gap: 12,
  },
  h2: { margin: 0, fontSize: 20, color: "#fff" },
  balance: { marginTop: 6, fontSize: 26, fontWeight: 800, color: "#fff" },
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
  hr: { height: 1, background: "rgba(255,255,255,0.12)", margin: "14px 0" },
  h3: { margin: "0 0 10px 0", fontSize: 14, color: "#cbd5e1" },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(3, 1fr)",
    gap: 12,
  },
  txn: {
    padding: 12,
    borderRadius: 12,
    background: "rgba(0,0,0,0.25)",
    border: "1px solid rgba(255,255,255,0.08)",
  },
  txnTitle: { fontWeight: 700, marginBottom: 6, color: "#e5e7eb" },
  txnAmt: { fontWeight: 800, color: "#fff" },
  txnDate: { fontSize: 12, color: "#94a3b8" },
};
