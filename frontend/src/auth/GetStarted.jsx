import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function GetStarted() {
  const navigate = useNavigate();

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

  return (
    <div style={styles.screen}>
      {/* Brand removed as requested */}

      {/* Floating Cards */}
      <div style={styles.cardsWrap}>
        <div style={{ ...styles.card, ...styles.cardBack }}>
          <div style={styles.cardRowTop}>
            <div style={styles.chip} />
            <div style={styles.brandMark}>VISA</div>
          </div>
          <div style={styles.cardNumber}>••••  ••••  ••••  2741</div>
          <div style={styles.cardRowBottom}>
            <div>
              <div style={styles.smallLabel}>Card Holder</div>
              <div style={styles.boldText}>ALEX JOHNSON</div>
            </div>
            <div>
              <div style={styles.smallLabel}>Expiry</div>
              <div style={styles.boldText}>09/27</div>
            </div>
          </div>
        </div>

        <div style={{ ...styles.card, ...styles.cardFront }}>
          <div style={styles.cardRowTop}>
            <div style={styles.chip} />
            <div style={styles.brandMark}>MASTERCARD</div>
          </div>
          <div style={styles.cardNumber}>5234  82••  ••64  9912</div>
          <div style={styles.cardRowBottom}>
            <div>
              <div style={styles.smallLabel}>Card Holder</div>
              <div style={styles.boldText}>SHAKTI</div>
            </div>
            <div>
              <div style={styles.smallLabel}>Expiry</div>
              <div style={styles.boldText}>02/28</div>
            </div>
          </div>
        </div>
      </div>

      {/* Headline & Description */}
      <div style={styles.textWrap}>
        <h1 style={styles.headline}>Easy banking simple way</h1>
        <p style={styles.subtext}>
          Experience secure, modern digital banking with instant transfers, smart insights, and seamless control.
        </p>
      </div>

      {/* CTA */}
      <button type="button" onClick={() => navigate("/login")} style={styles.cta}>
        <span>Get Started</span>
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden>
          <path d="M5 12h14" stroke="white" strokeWidth="2" strokeLinecap="round"/>
          <path d="M13 5l7 7-7 7" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
      </button>
    </div>
  );
}

const styles = {
  screen: {
    position: "fixed",
    inset: 0,
    width: "100vw",
    height: "100vh",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    padding: 16,
    boxSizing: "border-box",
    color: "#fff",
    background:
      "radial-gradient(1200px 600px at 50% -20%, rgba(120,119,198,0.35), transparent), linear-gradient(180deg, #0b0b13 0%, #0a0f2a 40%, #0b0633 100%)",
    overflow: "hidden",
    overscrollBehavior: "none",
  },
  cardsWrap: {
    position: "relative",
    width: "min(88vw, 520px)",
    maxWidth: "520px",
    height: 260,
    // Lift the cards a bit and add more space below so text isn't obscured
    marginTop: -16,
    marginBottom: 56,
  },
  card: {
    position: "absolute",
    width: "100%",
    height: 220,
    borderRadius: 18,
    padding: 16,
    boxShadow: "0 20px 50px rgba(0,0,0,0.45)",
    backdropFilter: "blur(8px)",
    WebkitBackdropFilter: "blur(8px)",
    border: "1px solid rgba(255,255,255,0.12)",
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    color: "#f8fafc",
  },
  cardBack: {
    transform: "rotate(-9deg) translate(-10px, 16px)",
    background: "linear-gradient(135deg, rgba(99,102,241,0.25), rgba(236,72,153,0.25))",
  },
  cardFront: {
    transform: "rotate(6deg) translate(12px, -6px)",
    background: "linear-gradient(135deg, rgba(124,58,237,0.6), rgba(236,72,153,0.45))",
  },
  chip: {
    width: 34,
    height: 24,
    borderRadius: 6,
    background: "linear-gradient(180deg,#facc15,#eab308)",
    boxShadow: "inset 0 0 0 1px rgba(0,0,0,0.15)",
  },
  brandMark: {
    fontSize: 12,
    fontWeight: 700,
    letterSpacing: 1,
    opacity: 0.9,
  },
  cardRowTop: {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
  },
  cardRowBottom: {
    display: "flex",
    alignItems: "flex-end",
    justifyContent: "space-between",
  },
  smallLabel: {
    fontSize: 10,
    opacity: 0.7,
  },
  boldText: {
    fontSize: 12,
    fontWeight: 700,
    letterSpacing: 0.6,
  },
  cardNumber: {
    fontSize: 20,
    letterSpacing: 2,
    fontFeatureSettings: '"tnum" on, "lnum" on',
    textShadow: "0 2px 6px rgba(0,0,0,0.35)",
  },
  textWrap: {
    textAlign: "center",
    maxWidth: 380,
  },
  headline: {
    margin: "0 0 8px 0",
    fontSize: 28,
    fontWeight: 800,
    letterSpacing: 0.2,
  },
  subtext: {
    margin: 0,
    fontSize: 14,
    lineHeight: 1.6,
    color: "#cbd5e1",
  },
  cta: {
    marginTop: 24,
    display: "inline-flex",
    alignItems: "center",
    gap: 10,
    padding: "12px 18px",
    borderRadius: 9999,
    border: "1px solid rgba(255,255,255,0.18)",
    background:
      "radial-gradient(120% 120% at 0% 0%, rgba(255,255,255,0.15), rgba(255,255,255,0) 40%), linear-gradient(90deg, #7c3aed, #ec4899)",
    color: "#fff",
    fontWeight: 800,
    letterSpacing: 0.2,
    cursor: "pointer",
    boxShadow: "0 10px 30px rgba(236,72,153,0.35), 0 6px 16px rgba(124,58,237,0.3)",
  },
};
