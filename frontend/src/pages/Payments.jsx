import React, { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Payments() {
  const navigate = useNavigate();
  // lock scroll like other pages
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

  // form state
  const [method, setMethod] = useState(null); // null | 'bank' | 'upi' | 'card'
  const [fromAccount, setFromAccount] = useState("");
  const [receiver, setReceiver] = useState(""); // to account (bank)
  const [upiId, setUpiId] = useState("");
  const [cardNumber, setCardNumber] = useState("");
  const [cardExpiry, setCardExpiry] = useState("");
  const [cvv, setCvv] = useState("");
  const [amount, setAmount] = useState("");
  const [remarks, setRemarks] = useState("");
  const [submitting, setSubmitting] = useState(false);

  // clear hidden fields when method changes
  useEffect(() => {
    if (!method) return;
    if (method === "bank") {
      setUpiId("");
      setCardNumber("");
      setCardExpiry("");
      setCvv("");
    } else if (method === "upi") {
      setReceiver("");
      setCardNumber("");
      setCardExpiry("");
      setCvv("");
    } else if (method === "card") {
      setReceiver("");
      setUpiId("");
    }
  }, [method]);

  const isValid = useMemo(() => {
    if (!method) return false;
    const amt = parseFloat(String(amount || "").trim());
    const okAmount = !Number.isNaN(amt) && amt > 0;
    if (!okAmount) return false;
    if (method === "bank") return Boolean(fromAccount && receiver.trim());
    if (method === "upi") return Boolean(fromAccount && upiId.trim());
    if (method === "card") return Boolean(cardNumber.trim() && cardExpiry.trim() && cvv.trim());
    return false;
  }, [method, fromAccount, receiver, upiId, cardNumber, cardExpiry, cvv, amount]);

  return (
    <div style={styles.screen}>
      <main style={styles.container}>
        <section style={styles.card}>
          <h1 style={styles.title}>Make a Payment</h1>
          <p style={styles.subtitle}>Transfer money securely</p>
          <div className="form-inner" style={{ marginTop: 12 }}>
            {/* Payment Method (choose first) */}
            <div style={{ margin: "0 0 8px", textAlign: "left", color: "#e2e8f0", fontSize: 14, fontWeight: 600 }}>Payment Method</div>
            <div style={styles.methodRow}>
              <label style={styles.radioLabel}>
                <input type="radio" name="method" checked={method === "bank"} onChange={() => setMethod("bank")} />
                <span>Bank Transfer</span>
              </label>
              <label style={styles.radioLabel}>
                <input type="radio" name="method" checked={method === "upi"} onChange={() => setMethod("upi")} />
                <span>UPI</span>
              </label>
              <label style={styles.radioLabel}>
                <input type="radio" name="method" checked={method === "card"} onChange={() => setMethod("card")} />
                <span>Card</span>
              </label>
            </div>

            {/* The rest of the form is hidden until a method is chosen */}
            {method ? (
              <>
            {/* From Account */}
            {method !== "card" ? (
              <>
                <label htmlFor="fromAccount" className="field-label">From Account</label>
                <div className="input-wrap" style={{ marginBottom: 16 }}>
                  <input
                    id="fromAccount"
                    type="text"
                    className="input"
                    placeholder="Account number (e.g., **** 1234)"
                    value={fromAccount}
                    onChange={(e) => setFromAccount(e.target.value)}
                  />
                </div>
              </>
            ) : null}

            {/* Receiver / Beneficiary */}
            {method === "bank" ? (
              <>
                <label htmlFor="receiver" className="field-label">To (Receiver / Beneficiary)</label>
                <div className="input-wrap">
                  <input
                    id="receiver"
                    type="text"
                    className="input"
                    placeholder="Receiver account number"
                    value={receiver}
                    onChange={(e) => setReceiver(e.target.value)}
                  />
                </div>
              </>
            ) : null}

            {/* UPI ID */}
            {method === "upi" ? (
              <>
                <label htmlFor="upiId" className="field-label">UPI ID</label>
                <div className="input-wrap">
                  <input
                    id="upiId"
                    type="text"
                    className="input"
                    placeholder="name@bank"
                    value={upiId}
                    onChange={(e) => setUpiId(e.target.value)}
                  />
                </div>
              </>
            ) : null}

            {/* Card fields */}
            {method === "card" ? (
              <>
                <label htmlFor="cardNumber" className="field-label">Card Number</label>
                <div className="input-wrap">
                  <input
                    id="cardNumber"
                    type="text"
                    className="input"
                    placeholder="1234 5678 9012 3456"
                    value={cardNumber}
                    onChange={(e) => setCardNumber(e.target.value)}
                  />
                </div>
                <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}>
                  <div className="input-wrap">
                    <label htmlFor="cardExpiry" className="field-label">Expiry</label>
                    <input
                      id="cardExpiry"
                      type="text"
                      className="input"
                      placeholder="MM/YY"
                      value={cardExpiry}
                      onChange={(e) => setCardExpiry(e.target.value)}
                    />
                  </div>
                  <div className="input-wrap">
                    <label htmlFor="cvv" className="field-label">CVV</label>
                    <input
                      id="cvv"
                      type="password"
                      className="input"
                      placeholder="***"
                      value={cvv}
                      onChange={(e) => setCvv(e.target.value)}
                    />
                  </div>
                </div>
                <div style={{ fontSize: 12, color: "#94a3b8", marginTop: -6, marginBottom: 8 }}>CVV is a 3-digit number</div>
              </>
            ) : null}

            {/* Amount */}
            <label htmlFor="amount" className="field-label">Amount</label>
            <div className="input-wrap">
              <input
                id="amount"
                type="number"
                className="input"
                placeholder="Enter amount"
                min="0"
                step="0.01"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
              />
            </div>

            </>
            ) : null}

            {/* Remarks */}
            <label htmlFor="remarks" className="field-label">Remarks</label>
            <div className="input-wrap">
              <textarea id="remarks" className="input" rows="3" placeholder="Add a note (optional)" style={{ padding: 12, resize: "vertical" }} value={remarks} onChange={(e) => setRemarks(e.target.value)} />
            </div>

            {/* CTA */}
            <button
              type="button"
              className="signin-btn"
              style={{ width: "100%", marginTop: 6, opacity: isValid && !submitting ? 1 : 0.7, cursor: isValid && !submitting ? "pointer" : "not-allowed" }}
              disabled={!isValid || submitting}
              onClick={() => {
                if (!isValid || submitting) return;
                setSubmitting(true);
                const now = new Date();
                const tx = {
                  id: `TXN-${now.getTime()}`,
                  amount: `$${Number(amount).toFixed(2)}`,
                  receiver: method === 'bank' ? receiver : method === 'upi' ? upiId : 'Card Payment',
                  method: method === 'bank' ? 'Bank Transfer' : method === 'upi' ? 'UPI' : 'Card',
                  when: now.toLocaleString(),
                  whenISO: now.toISOString(),
                  whenTs: now.getTime(),
                  status: 'Success',
                };
                try {
                  const raw = localStorage.getItem('dbp_txns');
                  const list = raw ? JSON.parse(raw) : [];
                  list.push(tx);
                  localStorage.setItem('dbp_txns', JSON.stringify(list));
                } catch (_) { /* noop */ }
                setTimeout(() => {
                  setSubmitting(false);
                  navigate('/payments/success', { state: tx });
                }, 1200);
              }}
            >
              {submitting ? (
                <span style={{ display: 'inline-flex', alignItems: 'center', gap: 8 }}>
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ animation: 'spin 1s linear infinite' }}>
                    <path d="M12 3a9 9 0 1 0 9 9" stroke="white" strokeWidth="2" strokeLinecap="round"/>
                  </svg>
                  Processing...
                </span>
              ) : (
                'Pay Now'
              )}
            </button>
            <button
              type="button"
              onClick={() => navigate(-1)}
              style={{
                marginTop: 10,
                width: '100%',
                padding: '10px 14px',
                borderRadius: 9999,
                background: 'rgba(255,255,255,0.08)',
                border: '1px solid rgba(255,255,255,0.18)',
                color: '#e5e7eb',
                cursor: 'pointer',
                fontWeight: 700,
              }}
            >
              Back
            </button>
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
  container: {
    maxWidth: 800,
    margin: "0 auto",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    height: "100%",
  },
  card: {
    width: "100%",
    maxWidth: 520,
    padding: 24,
    borderRadius: 16,
    background: "rgba(255,255,255,0.08)",
    border: "1px solid rgba(255,255,255,0.12)",
    boxShadow: "0 20px 50px rgba(0,0,0,0.45)",
    backdropFilter: "blur(10px)",
    WebkitBackdropFilter: "blur(10px)",
    textAlign: "center",
  },
  title: { margin: 0, color: "#fff", fontSize: 26, fontWeight: 800 },
  subtitle: { margin: "8px 0 0 0", color: "#cbd5e1", fontSize: 14 },
  methodRow: {
    display: "flex",
    gap: 10,
    justifyContent: "center",
    marginBottom: 10,
  },
  radioLabel: {
    display: "inline-flex",
    alignItems: "center",
    gap: 6,
    background: "rgba(255,255,255,0.08)",
    border: "1px solid rgba(255,255,255,0.18)",
    color: "#e5e7eb",
    padding: "8px 12px",
    borderRadius: 9999,
    cursor: "pointer",
  },
};
