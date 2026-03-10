import { useState } from "react";
import { MainLayout } from "@/components/layout/MainLayout";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { 
  User, 
  Bell, 
  Link2, 
  Shield, 
  Palette,
  Youtube,
  Instagram,
  Check,
  ExternalLink
} from "lucide-react";
import { cn } from "@/lib/utils";

const settingsTabs = [
  { id: "profile", label: "Perfil", icon: User },
  { id: "notifications", label: "Notificaciones", icon: Bell },
  { id: "connections", label: "Conexiones", icon: Link2 },
  { id: "security", label: "Seguridad", icon: Shield },
  { id: "appearance", label: "Apariencia", icon: Palette },
];

const socialConnections = [
  { 
    id: "youtube", 
    name: "YouTube", 
    icon: Youtube, 
    connected: true,
    account: "@tucanal",
    color: "bg-red-500"
  },
  { 
    id: "instagram", 
    name: "Instagram", 
    icon: Instagram, 
    connected: true,
    account: "@tucuenta",
    color: "bg-gradient-to-br from-purple-500 to-pink-500"
  },
  { 
    id: "tiktok", 
    name: "TikTok", 
    icon: () => (
      <svg className="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
        <path d="M19.59 6.69a4.83 4.83 0 0 1-3.77-4.25V2h-3.45v13.67a2.89 2.89 0 0 1-5.2 1.74 2.89 2.89 0 0 1 2.31-4.64 2.93 2.93 0 0 1 .88.13V9.4a6.84 6.84 0 0 0-1-.05A6.33 6.33 0 0 0 5 20.1a6.34 6.34 0 0 0 10.86-4.43v-7a8.16 8.16 0 0 0 4.77 1.52v-3.4a4.85 4.85 0 0 1-1-.1z" />
      </svg>
    ), 
    connected: false,
    account: null,
    color: "bg-foreground"
  },
];

const Settings = () => {
  const [activeTab, setActiveTab] = useState("profile");

  return (
    <MainLayout 
      title="Configuración" 
      subtitle="Gestiona tu cuenta y preferencias"
    >
      <div className="flex flex-col lg:flex-row gap-6">
        {/* Sidebar */}
        <div className="lg:w-64 flex-shrink-0">
          <div className="glass rounded-2xl p-4">
            <nav className="space-y-1">
              {settingsTabs.map((tab) => (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id)}
                  className={cn(
                    "w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-colors",
                    activeTab === tab.id
                      ? "bg-primary text-primary-foreground"
                      : "text-muted-foreground hover:bg-secondary hover:text-foreground"
                  )}
                >
                  <tab.icon className="w-5 h-5" />
                  <span className="font-medium">{tab.label}</span>
                </button>
              ))}
            </nav>
          </div>
        </div>

        {/* Content */}
        <div className="flex-1">
          {activeTab === "profile" && (
            <div className="glass rounded-2xl p-6 space-y-6">
              <div>
                <h3 className="text-lg font-semibold text-foreground mb-4">Información del Perfil</h3>
                <div className="flex items-center gap-6 mb-6">
                  <div className="w-20 h-20 rounded-full gradient-primary flex items-center justify-center">
                    <User className="w-10 h-10 text-primary-foreground" />
                  </div>
                  <Button variant="outline">Cambiar foto</Button>
                </div>
                
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="name">Nombre</Label>
                    <Input id="name" defaultValue="Tu Nombre" />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="email">Email</Label>
                    <Input id="email" type="email" defaultValue="tu@email.com" />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="company">Empresa</Label>
                    <Input id="company" defaultValue="Tu Empresa" />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="website">Sitio Web</Label>
                    <Input id="website" defaultValue="https://tusitio.com" />
                  </div>
                </div>
              </div>

              <div className="pt-4 border-t border-border">
                <Button className="gradient-primary">Guardar Cambios</Button>
              </div>
            </div>
          )}

          {activeTab === "notifications" && (
            <div className="glass rounded-2xl p-6 space-y-6">
              <h3 className="text-lg font-semibold text-foreground">Notificaciones</h3>
              
              <div className="space-y-4">
                <div className="flex items-center justify-between p-4 rounded-xl bg-secondary/50">
                  <div>
                    <p className="font-medium text-foreground">Publicaciones programadas</p>
                    <p className="text-sm text-muted-foreground">Recibe alertas antes de publicar</p>
                  </div>
                  <Switch defaultChecked />
                </div>
                
                <div className="flex items-center justify-between p-4 rounded-xl bg-secondary/50">
                  <div>
                    <p className="font-medium text-foreground">Rendimiento semanal</p>
                    <p className="text-sm text-muted-foreground">Resumen de analytics cada semana</p>
                  </div>
                  <Switch defaultChecked />
                </div>
                
                <div className="flex items-center justify-between p-4 rounded-xl bg-secondary/50">
                  <div>
                    <p className="font-medium text-foreground">Nuevos comentarios</p>
                    <p className="text-sm text-muted-foreground">Alerta cuando recibes comentarios</p>
                  </div>
                  <Switch />
                </div>
                
                <div className="flex items-center justify-between p-4 rounded-xl bg-secondary/50">
                  <div>
                    <p className="font-medium text-foreground">Sugerencias IA</p>
                    <p className="text-sm text-muted-foreground">Ideas para mejorar tu contenido</p>
                  </div>
                  <Switch defaultChecked />
                </div>
              </div>
            </div>
          )}

          {activeTab === "connections" && (
            <div className="glass rounded-2xl p-6 space-y-6">
              <h3 className="text-lg font-semibold text-foreground">Conexiones de Redes Sociales</h3>
              
              <div className="space-y-4">
                {socialConnections.map((connection) => (
                  <div 
                    key={connection.id}
                    className="flex items-center justify-between p-4 rounded-xl bg-secondary/50"
                  >
                    <div className="flex items-center gap-4">
                      <div className={cn(
                        "w-12 h-12 rounded-xl flex items-center justify-center",
                        connection.color
                      )}>
                        <connection.icon className="w-6 h-6 text-primary-foreground" />
                      </div>
                      <div>
                        <p className="font-medium text-foreground">{connection.name}</p>
                        {connection.connected ? (
                          <p className="text-sm text-muted-foreground">{connection.account}</p>
                        ) : (
                          <p className="text-sm text-muted-foreground">No conectado</p>
                        )}
                      </div>
                    </div>
                    
                    {connection.connected ? (
                      <div className="flex items-center gap-2">
                        <span className="flex items-center gap-1 text-sm text-success">
                          <Check className="w-4 h-4" />
                          Conectado
                        </span>
                        <Button variant="ghost" size="sm">
                          Desconectar
                        </Button>
                      </div>
                    ) : (
                      <Button variant="outline" className="gap-2">
                        <ExternalLink className="w-4 h-4" />
                        Conectar
                      </Button>
                    )}
                  </div>
                ))}
              </div>
            </div>
          )}

          {activeTab === "security" && (
            <div className="glass rounded-2xl p-6 space-y-6">
              <h3 className="text-lg font-semibold text-foreground">Seguridad</h3>
              
              <div className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="current-password">Contraseña Actual</Label>
                  <Input id="current-password" type="password" />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="new-password">Nueva Contraseña</Label>
                  <Input id="new-password" type="password" />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="confirm-password">Confirmar Contraseña</Label>
                  <Input id="confirm-password" type="password" />
                </div>
              </div>

              <div className="pt-4 border-t border-border">
                <Button className="gradient-primary">Actualizar Contraseña</Button>
              </div>

              <div className="pt-4 border-t border-border">
                <div className="flex items-center justify-between p-4 rounded-xl bg-secondary/50">
                  <div>
                    <p className="font-medium text-foreground">Autenticación de dos factores</p>
                    <p className="text-sm text-muted-foreground">Añade una capa extra de seguridad</p>
                  </div>
                  <Switch />
                </div>
              </div>
            </div>
          )}

          {activeTab === "appearance" && (
            <div className="glass rounded-2xl p-6 space-y-6">
              <h3 className="text-lg font-semibold text-foreground">Apariencia</h3>
              
              <div className="space-y-4">
                <div className="flex items-center justify-between p-4 rounded-xl bg-secondary/50">
                  <div>
                    <p className="font-medium text-foreground">Tema Oscuro</p>
                    <p className="text-sm text-muted-foreground">Activado por defecto</p>
                  </div>
                  <Switch defaultChecked />
                </div>

                <div className="flex items-center justify-between p-4 rounded-xl bg-secondary/50">
                  <div>
                    <p className="font-medium text-foreground">Animaciones</p>
                    <p className="text-sm text-muted-foreground">Efectos visuales en la interfaz</p>
                  </div>
                  <Switch defaultChecked />
                </div>

                <div className="flex items-center justify-between p-4 rounded-xl bg-secondary/50">
                  <div>
                    <p className="font-medium text-foreground">Sidebar compacto</p>
                    <p className="text-sm text-muted-foreground">Mostrar solo iconos</p>
                  </div>
                  <Switch />
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </MainLayout>
  );
};

export default Settings;
