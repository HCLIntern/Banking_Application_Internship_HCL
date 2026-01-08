import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "../auth/Login";
import SignUp from "../auth/SignUp";
import Dashboard from "../pages/Dashboard";
import Accounts from "../pages/Accounts";
import AdminDashboard from "../pages/AdminDashboard";
import GetStarted from "../auth/GetStarted";
import Payments from "../pages/Payments";
import PaymentSuccess from "../pages/PaymentSuccess";
import PaymentHistory from "../pages/PaymentHistory";

function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<GetStarted />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/payments" element={<Payments />} />
        <Route path="/payments/success" element={<PaymentSuccess />} />
        <Route path="/payments/history" element={<PaymentHistory />} />
        <Route path="/accounts" element={<Accounts />} />
        <Route path="/admin" element={<AdminDashboard />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;
