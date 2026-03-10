import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "@/context/AuthContext";

import Index from "./pages/dashboard/Index";
import Videos from "./pages/dashboard/Videos";
import Editor from "./pages/dashboard/Editor";
import CalendarPage from "./pages/dashboard/CalendarPage";
import Analytics from "./pages/dashboard/Analytics";
import Settings from "./pages/dashboard/Settings";
import UploadVideo from "./pages/dashboard/UploadVideo";
import NotFound from "./pages/NotFound";
import ForgotPassword from "./pages/public/ForgotPassword";


import Login from "./pages/public/Login";
import Register from "./pages/public/Register";
import ProtectedRoute from "./components/ProtectedRoute";

const queryClient = new QueryClient();

const App = () => (
  <AuthProvider> {/* 🔐 ENVOLVER TODO */}
    <QueryClientProvider client={queryClient}>
      <TooltipProvider>
        <div className="dark">
          <Toaster />
          <Sonner />
          <BrowserRouter>
            <Routes>

              {/* PUBLIC ROUTES */}
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/forgot-password" element={<ForgotPassword />} />


              {/* PRIVATE ROUTES */}
              <Route element={<ProtectedRoute />}>
                <Route path="/" element={<Index />} />
                <Route path="/videos" element={<Videos />} />
                <Route path="/upload" element={<UploadVideo />} />
                <Route path="/editor" element={<Editor />} />
                <Route path="/calendar" element={<CalendarPage />} />
                <Route path="/analytics" element={<Analytics />} />
                <Route path="/settings" element={<Settings />} />
              </Route>

              {/* 404 */}
              <Route path="*" element={<NotFound />} />

            </Routes>
          </BrowserRouter>
        </div>
      </TooltipProvider>
    </QueryClientProvider>
  </AuthProvider>
);

export default App;
