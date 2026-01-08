import React, { useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function PaymentHistory() {
  const navigate = useNavigate();
  const { state } = useLocation();

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
  // Gate access: only allow when coming from Dashboard History button
  useEffect(() => {
    if (!state || state.fromDashboard !== true) {
      navigate("/dashboard", { replace: true });
    }
  }, [state, navigate]);

  const [fromDate, setFromDate] = useState("");
  const [toDate, setToDate] = useState("");
  const [filterApplied, setFilterApplied] = useState(false);

  const allTxns = useMemo(() => {
    try {
      const raw = localStorage.getItem("dbp_txns");
      const list = raw ? JSON.parse(raw) : [];
      return Array.isArray(list) ? list : [];
    } catch {
      return [];
    }
  }, []);

  const sorted = useMemo(() => {
    return [...allTxns].sort((a, b) => (b.whenTs || 0) - (a.whenTs || 0));
  }, [allTxns]);

  const filtered = useMemo(() => {
    if (!filterApplied || (!fromDate && !toDate)) {
      return sorted; // default: show all
    }
    const fromTs = fromDate ? new Date(fromDate).setHours(0, 0, 0, 0) : -Infinity;
    const toTs = toDate ? new Date(toDate).setHours(23, 59, 59, 999) : Infinity;
    return sorted.filter((t) => {
      const ts = t.whenTs || Date.parse(t.whenISO || t.when || 0);
      return ts >= fromTs && ts <= toTs;
    });
  }, [sorted, filterApplied, fromDate, toDate]);

  const countInfo = `${Math.min(filtered.length, 5)} of ${sorted.length}`;

  const downloadCsv = () => {
    const header = [
      "Transaction ID",
      "Date",
      "Receiver",
      "Method",
      "Amount",
      "Status",
    ];
    const rows = filtered.map((t) => [
      t.id,
      t.when,
      t.receiver,
      t.method,
      t.amount,
      t.status || "Success",
    ]);
    const csv = [header, ...rows].map((r) => r.map((x) => `"${String(x ?? "").replace(/"/g, '""')}"`).join(",")).join("\n");
    const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `payment-statement-${Date.now()}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  };

  return (
    <div style={styles.screen}>
      <main style={styles.container}>
        <section style={styles.card}>
          <h1 style={styles.title}>Payment History</h1>
          <p style={styles.subtitle}>View and download your transactions</p>

          {/* Filters */}
          <div style={styles.filters}>
            <div style={styles.filterCol}>
              <label htmlFor="fromDate" className="field-label">From Date</label>
              <input id="fromDate" type="date" className="input" value={fromDate} onChange={(e) => setFromDate(e.target.value)} />
            </div>
            <div style={styles.filterCol}>
              <label htmlFor="toDate" className="field-label">To Date</label>
              <input id="toDate" type="date" className="input" value={toDate} onChange={(e) => setToDate(e.target.value)} />
            </div>
            <div style={styles.filterActions}>
              <button className="signin-btn" onClick={() => setFilterApplied(true)}>Apply Filter</button>
              <button style={styles.ghostBtn} onClick={() => { setFromDate(""); setToDate(""); setFilterApplied(false); }}>Reset</button>
            </div>
          </div>

          {/* Actions */}
          <div style={styles.topActions}>
            <div style={{ color: '#cbd5e1', fontSize: 13 }}>Showing {filtered.length} of {sorted.length} transactions</div>
            <div style={{ display: 'flex', gap: 8 }}>
              <button className="signin-btn" onClick={downloadCsv}>Download Statement</button>
              <button style={styles.ghostBtn} onClick={() => navigate(-1)}>Go Back</button>
            </div>
          </div>

          {/* List */}
          <div style={styles.list}>
            {filtered.length === 0 ? (
              <div style={styles.empty}>No transactions found for the selected date range</div>
            ) : (
              filtered.map((t) => (
                <div key={t.id} style={styles.rowCard}>
                  <div style={styles.rowMain}>
                    <div style={styles.rowTitle}>{t.receiver}</div>
                    <div style={styles.rowMeta}>{t.method}</div>
                  </div>
                  <div style={styles.rowSide}>
                    <div style={styles.rowDate}>{t.when}</div>
                    <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
                      <div style={{ fontWeight: 800, color: '#fff' }}>{t.amount}</div>
                      <span style={{ ...styles.badge, ...(t.status === 'Failed' ? styles.badgeRed : t.status === 'Pending' ? styles.badgeGray : styles.badgeGreen) }}>
                        {t.status || 'Success'}
                      </span>
                    </div>
                  </div>
                </div>
              ))
            )}
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
  container: { height: "100%", display: "flex", alignItems: "center", justifyContent: "center" },
  card: {
    width: "100%",
    maxWidth: 900,
    padding: 24,
    borderRadius: 16,
    background: "rgba(255,255,255,0.08)",
    border: "1px solid rgba(255,255,255,0.12)",
    boxShadow: "0 20px 50px rgba(0,0,0,0.45)",
    backdropFilter: "blur(10px)",
    WebkitBackdropFilter: "blur(10px)",
  },
  title: { margin: 0, color: "#fff", fontSize: 24, fontWeight: 800 },
  subtitle: { margin: "6px 0 16px", color: "#cbd5e1" },
  filters: { display: 'grid', gridTemplateColumns: '1fr 1fr auto', gap: 10, alignItems: 'end', marginBottom: 12 },
  filterCol: {},
  filterActions: { display: 'flex', gap: 8 },
  topActions: { display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 12 },
  list: { display: 'flex', flexDirection: 'column', gap: 10, maxHeight: '52vh', overflow: 'auto', paddingRight: 6 },
  rowCard: { display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: 12, borderRadius: 12, background: 'rgba(0,0,0,0.25)', border: '1px solid rgba(255,255,255,0.08)' },
  rowMain: {},
  rowTitle: { fontWeight: 700, color: '#e5e7eb' },
  rowMeta: { fontSize: 12, color: '#94a3b8' },
  rowSide: { textAlign: 'right' },
  rowDate: { fontSize: 12, color: '#94a3b8', marginBottom: 4 },
  badge: { padding: '4px 8px', borderRadius: 9999, fontSize: 12, fontWeight: 700, border: '1px solid transparent' },
  badgeGreen: { background: 'rgba(16,185,129,0.18)', borderColor: 'rgba(16,185,129,0.35)', color: '#10b981' },
  badgeRed: { background: 'rgba(239,68,68,0.18)', borderColor: 'rgba(239,68,68,0.35)', color: '#ef4444' },
  badgeGray: { background: 'rgba(148,163,184,0.18)', borderColor: 'rgba(148,163,184,0.35)', color: '#cbd5e1' },
  ghostBtn: { padding: '10px 14px', borderRadius: 9999, background: 'rgba(255,255,255,0.08)', border: '1px solid rgba(255,255,255,0.18)', color: '#e5e7eb', cursor: 'pointer', fontWeight: 700 },
};
