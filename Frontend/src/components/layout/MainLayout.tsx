import { ReactNode } from "react";
import { Sidebar } from "./Sidebar";
import { Header } from "./Header";

interface MainLayoutProps {
  children: ReactNode;
  title: string;
  subtitle?: string;
}

export const MainLayout = ({ children, title, subtitle }: MainLayoutProps) => {
  return (
    <div className="min-h-screen bg-background dark">
      <Sidebar />
      <main className="ml-64 transition-all duration-300">
        <Header title={title} subtitle={subtitle} />
        <div className="p-8">
          {children}
        </div>
      </main>
    </div>
  );
};
