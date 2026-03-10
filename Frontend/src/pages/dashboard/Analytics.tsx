import { MainLayout } from "@/components/layout/MainLayout";
import { StatsCard } from "@/components/dashboard/StatsCard";
import { AnalyticsChart } from "@/components/dashboard/AnalyticsChart";
import { 
  Eye, 
  Heart, 
  MessageCircle, 
  Share2, 
  TrendingUp,
  Users,
  Clock,
  Target
} from "lucide-react";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from "recharts";

const platformData = [
  { name: "YouTube", value: 45, color: "hsl(0, 72%, 51%)" },
  { name: "Instagram", value: 30, color: "hsl(328, 85%, 60%)" },
  { name: "TikTok", value: 25, color: "hsl(220, 10%, 95%)" },
];

const contentPerformance = [
  { name: "Tutoriales", views: 45000, engagement: 8.5 },
  { name: "Tips", views: 32000, engagement: 12.3 },
  { name: "Behind Scenes", views: 28000, engagement: 15.2 },
  { name: "Entrevistas", views: 18000, engagement: 6.8 },
  { name: "Webinars", views: 12000, engagement: 4.2 },
];

const Analytics = () => {
  return (
    <MainLayout 
      title="Analytics" 
      subtitle="Analiza el rendimiento de tu contenido"
    >
      <div className="space-y-6">
        {/* Quick Stats */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <StatsCard
            title="Vistas Totales"
            value="124.5K"
            change="+23% vs. mes anterior"
            changeType="positive"
            icon={Eye}
            gradient="primary"
          />
          <StatsCard
            title="Engagement Rate"
            value="8.7%"
            change="+1.2% esta semana"
            changeType="positive"
            icon={Heart}
            gradient="accent"
          />
          <StatsCard
            title="Comentarios"
            value="2.4K"
            change="+15% vs. mes anterior"
            changeType="positive"
            icon={MessageCircle}
            gradient="success"
          />
          <StatsCard
            title="Compartidos"
            value="890"
            change="+8% esta semana"
            changeType="positive"
            icon={Share2}
            gradient="primary"
          />
        </div>

        {/* Main Chart */}
        <AnalyticsChart />

        {/* Secondary Charts */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Platform Distribution */}
          <div className="glass rounded-2xl p-6">
            <h3 className="text-lg font-semibold text-foreground mb-6">
              Distribución por Plataforma
            </h3>
            <div className="flex items-center gap-8">
              <div className="h-[200px] flex-1">
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={platformData}
                      cx="50%"
                      cy="50%"
                      innerRadius={60}
                      outerRadius={80}
                      paddingAngle={5}
                      dataKey="value"
                    >
                      {platformData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={entry.color} />
                      ))}
                    </Pie>
                    <Tooltip
                      contentStyle={{
                        backgroundColor: "hsl(220, 25%, 12%)",
                        border: "1px solid hsl(220, 20%, 18%)",
                        borderRadius: "12px",
                      }}
                    />
                  </PieChart>
                </ResponsiveContainer>
              </div>
              <div className="space-y-4">
                {platformData.map((platform) => (
                  <div key={platform.name} className="flex items-center gap-3">
                    <div 
                      className="w-4 h-4 rounded-full" 
                      style={{ backgroundColor: platform.color }}
                    />
                    <span className="text-sm text-foreground">{platform.name}</span>
                    <span className="text-sm font-semibold text-muted-foreground ml-auto">
                      {platform.value}%
                    </span>
                  </div>
                ))}
              </div>
            </div>
          </div>

          {/* Content Performance */}
          <div className="glass rounded-2xl p-6">
            <h3 className="text-lg font-semibold text-foreground mb-6">
              Rendimiento por Tipo de Contenido
            </h3>
            <div className="h-[200px]">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={contentPerformance} layout="vertical">
                  <CartesianGrid strokeDasharray="3 3" stroke="hsl(220, 20%, 18%)" />
                  <XAxis 
                    type="number" 
                    stroke="hsl(220, 10%, 55%)"
                    fontSize={12}
                    tickFormatter={(value) => `${value / 1000}k`}
                  />
                  <YAxis 
                    type="category" 
                    dataKey="name" 
                    stroke="hsl(220, 10%, 55%)"
                    fontSize={12}
                    width={80}
                  />
                  <Tooltip
                    contentStyle={{
                      backgroundColor: "hsl(220, 25%, 12%)",
                      border: "1px solid hsl(220, 20%, 18%)",
                      borderRadius: "12px",
                    }}
                  />
                  <Bar 
                    dataKey="views" 
                    fill="hsl(220, 70%, 55%)" 
                    radius={[0, 4, 4, 0]}
                  />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
        </div>

        {/* Additional Metrics */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="glass rounded-2xl p-6">
            <div className="flex items-center gap-3 mb-3">
              <div className="w-10 h-10 rounded-xl bg-primary/20 flex items-center justify-center">
                <TrendingUp className="w-5 h-5 text-primary" />
              </div>
              <span className="text-sm font-medium text-muted-foreground">Mejor día</span>
            </div>
            <p className="text-2xl font-bold text-foreground">Martes</p>
            <p className="text-sm text-muted-foreground">Mayor engagement</p>
          </div>

          <div className="glass rounded-2xl p-6">
            <div className="flex items-center gap-3 mb-3">
              <div className="w-10 h-10 rounded-xl bg-accent/20 flex items-center justify-center">
                <Clock className="w-5 h-5 text-accent" />
              </div>
              <span className="text-sm font-medium text-muted-foreground">Mejor hora</span>
            </div>
            <p className="text-2xl font-bold text-foreground">2:00 PM</p>
            <p className="text-sm text-muted-foreground">Hora pico de vistas</p>
          </div>

          <div className="glass rounded-2xl p-6">
            <div className="flex items-center gap-3 mb-3">
              <div className="w-10 h-10 rounded-xl bg-success/20 flex items-center justify-center">
                <Users className="w-5 h-5 text-success" />
              </div>
              <span className="text-sm font-medium text-muted-foreground">Audiencia</span>
            </div>
            <p className="text-2xl font-bold text-foreground">25-34</p>
            <p className="text-sm text-muted-foreground">Rango de edad principal</p>
          </div>

          <div className="glass rounded-2xl p-6">
            <div className="flex items-center gap-3 mb-3">
              <div className="w-10 h-10 rounded-xl bg-warning/20 flex items-center justify-center">
                <Target className="w-5 h-5 text-warning" />
              </div>
              <span className="text-sm font-medium text-muted-foreground">CTR promedio</span>
            </div>
            <p className="text-2xl font-bold text-foreground">4.2%</p>
            <p className="text-sm text-muted-foreground">Click-through rate</p>
          </div>
        </div>
      </div>
    </MainLayout>
  );
};

export default Analytics;
