import { Upload, Scissors, Calendar, Sparkles } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Link } from "react-router-dom";

const actions = [
  {
    icon: Upload,
    label: "Subir Video",
    description: "Sube videos horizontales",
    path: "/videos",
    variant: "primary" as const,
  },
  {
    icon: Scissors,
    label: "Crear Short",
    description: "Convierte a formato vertical",
    path: "/editor",
    variant: "accent" as const,
  },
  {
    icon: Calendar,
    label: "Programar",
    description: "Agenda publicaciones",
    path: "/calendar",
    variant: "secondary" as const,
  },
  {
    icon: Sparkles,
    label: "IA Sugerencias",
    description: "Optimiza tu contenido",
    path: "/analytics",
    variant: "secondary" as const,
  },
];

export const QuickActions = () => {
  return (
    <div className="glass rounded-2xl p-6 animate-slide-up">
      <h3 className="text-lg font-semibold text-foreground mb-4">Acciones Rápidas</h3>
      
      <div className="grid grid-cols-2 gap-3">
        {actions.map((action) => (
          <Link
            key={action.label}
            to={action.path}
            className={`flex flex-col items-center gap-2 p-4 rounded-xl transition-all duration-200 hover:scale-105 group ${
              action.variant === "primary"
                ? "gradient-primary shadow-glow"
                : action.variant === "accent"
                ? "gradient-accent shadow-accent-glow"
                : "bg-secondary hover:bg-secondary/80"
            }`}
          >
            <action.icon className={`w-6 h-6 ${
              action.variant === "secondary" 
                ? "text-foreground" 
                : "text-primary-foreground"
            }`} />
            <span className={`text-sm font-medium text-center ${
              action.variant === "secondary" 
                ? "text-foreground" 
                : "text-primary-foreground"
            }`}>
              {action.label}
            </span>
          </Link>
        ))}
      </div>
    </div>
  );
};
