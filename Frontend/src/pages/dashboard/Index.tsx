import { MainLayout } from "@/components/layout/MainLayout";
import { StatsCard } from "@/components/dashboard/StatsCard";
import { VideoCard } from "@/components/dashboard/VideoCard";
import { AnalyticsChart } from "@/components/dashboard/AnalyticsChart";
import { UpcomingSchedule } from "@/components/dashboard/UpcomingSchedule";
import { QuickActions } from "@/components/dashboard/QuickActions";
import { Video, Users, TrendingUp, Zap } from "lucide-react";

const recentVideos = [
  {
    title: "Cómo escalar tu startup en 2024",
    thumbnail: "https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=400&h=225&fit=crop",
    duration: "12:34",
    views: "15.2K",
    likes: "1.2K",
    status: "published" as const,
    platform: "YouTube",
  },
  {
    title: "5 errores que cometen los emprendedores",
    thumbnail: "https://images.unsplash.com/photo-1552664730-d307ca884978?w=400&h=225&fit=crop",
    duration: "8:45",
    views: "8.7K",
    likes: "890",
    status: "published" as const,
    platform: "Instagram",
  },
  {
    title: "Tips de productividad para equipos remotos",
    thumbnail: "https://images.unsplash.com/photo-1600880292203-757bb62b4baf?w=400&h=225&fit=crop",
    duration: "15:20",
    views: "0",
    likes: "0",
    status: "scheduled" as const,
    platform: "YouTube",
  },
  {
    title: "Detrás de cámaras - Sesión de fotos",
    thumbnail: "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=400&h=225&fit=crop",
    duration: "0:58",
    views: "0",
    likes: "0",
    status: "draft" as const,
    platform: "TikTok",
  },
];

const Index = () => {
  return (
    <MainLayout 
      title="Dashboard" 
      subtitle="Bienvenido de vuelta. Aquí está el resumen de tu contenido."
    >
      <div className="space-y-8">
        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <StatsCard
            title="Total Videos"
            value="47"
            change="+5 este mes"
            changeType="positive"
            icon={Video}
            gradient="primary"
          />
          <StatsCard
            title="Vistas Totales"
            value="124.5K"
            change="+23% vs. mes anterior"
            changeType="positive"
            icon={TrendingUp}
            gradient="accent"
          />
          <StatsCard
            title="Seguidores"
            value="8.2K"
            change="+340 esta semana"
            changeType="positive"
            icon={Users}
            gradient="success"
          />
          <StatsCard
            title="Shorts Creados"
            value="23"
            change="12 publicados"
            changeType="neutral"
            icon={Zap}
            gradient="primary"
          />
        </div>

        {/* Main Content Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Analytics Chart - Takes 2 columns */}
          <div className="lg:col-span-2">
            <AnalyticsChart />
          </div>

          {/* Quick Actions */}
          <QuickActions />
        </div>

        {/* Videos and Schedule Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Recent Videos - Takes 2 columns */}
          <div className="lg:col-span-2 space-y-4">
            <div className="flex items-center justify-between">
              <h2 className="text-xl font-semibold text-foreground">Videos Recientes</h2>
              <button className="text-sm font-medium text-primary hover:text-primary/80 transition-colors">
                Ver todos
              </button>
            </div>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              {recentVideos.map((video, index) => (
                <VideoCard key={index} {...video} />
              ))}
            </div>
          </div>

          {/* Upcoming Schedule */}
          <UpcomingSchedule />
        </div>
      </div>
    </MainLayout>
  );
};

export default Index;
