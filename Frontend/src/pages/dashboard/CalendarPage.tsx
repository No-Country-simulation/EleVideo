import { useState } from "react";
import { MainLayout } from "@/components/layout/MainLayout";
import { Button } from "@/components/ui/button";
import { ChevronLeft, ChevronRight, Plus, Youtube, Instagram } from "lucide-react";
import { cn } from "@/lib/utils";

interface ScheduledPost {
  id: number;
  title: string;
  platform: "youtube" | "instagram" | "tiktok";
  time: string;
  type: "video" | "short";
}

interface DaySchedule {
  [key: number]: ScheduledPost[];
}

const scheduledPosts: DaySchedule = {
  5: [
    { id: 1, title: "Marketing Tutorial", platform: "youtube", time: "10:00", type: "video" },
  ],
  8: [
    { id: 2, title: "Tips rápidos", platform: "instagram", time: "14:00", type: "short" },
    { id: 3, title: "Behind the scenes", platform: "tiktok", time: "18:00", type: "short" },
  ],
  12: [
    { id: 4, title: "Webinar grabado", platform: "youtube", time: "09:00", type: "video" },
  ],
  15: [
    { id: 5, title: "Q&A Session", platform: "instagram", time: "16:00", type: "video" },
  ],
  20: [
    { id: 6, title: "Producto nuevo", platform: "tiktok", time: "12:00", type: "short" },
  ],
  25: [
    { id: 7, title: "Caso de éxito", platform: "youtube", time: "11:00", type: "video" },
    { id: 8, title: "Teaser", platform: "instagram", time: "20:00", type: "short" },
  ],
};

const platformColors = {
  youtube: "bg-red-500",
  instagram: "bg-gradient-to-br from-purple-500 to-pink-500",
  tiktok: "bg-foreground",
};

const CalendarPage = () => {
  const [currentDate, setCurrentDate] = useState(new Date());
  
  const daysInMonth = new Date(
    currentDate.getFullYear(),
    currentDate.getMonth() + 1,
    0
  ).getDate();
  
  const firstDayOfMonth = new Date(
    currentDate.getFullYear(),
    currentDate.getMonth(),
    1
  ).getDay();

  const monthNames = [
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
  ];

  const dayNames = ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"];

  const prevMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1));
  };

  const nextMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1));
  };

  const today = new Date();
  const isToday = (day: number) => 
    day === today.getDate() && 
    currentDate.getMonth() === today.getMonth() && 
    currentDate.getFullYear() === today.getFullYear();

  return (
    <MainLayout 
      title="Calendario" 
      subtitle="Programa y organiza tus publicaciones"
    >
      <div className="grid grid-cols-1 xl:grid-cols-4 gap-6">
        {/* Calendar */}
        <div className="xl:col-span-3">
          <div className="glass rounded-2xl p-6">
            {/* Calendar Header */}
            <div className="flex items-center justify-between mb-6">
              <div className="flex items-center gap-4">
                <Button variant="ghost" size="icon" onClick={prevMonth}>
                  <ChevronLeft className="w-5 h-5" />
                </Button>
                <h2 className="text-xl font-semibold text-foreground">
                  {monthNames[currentDate.getMonth()]} {currentDate.getFullYear()}
                </h2>
                <Button variant="ghost" size="icon" onClick={nextMonth}>
                  <ChevronRight className="w-5 h-5" />
                </Button>
              </div>
              <Button className="gradient-accent shadow-accent-glow">
                <Plus className="w-4 h-4 mr-2" />
                Nueva Publicación
              </Button>
            </div>

            {/* Day Names */}
            <div className="grid grid-cols-7 gap-2 mb-2">
              {dayNames.map((day) => (
                <div key={day} className="text-center py-2 text-sm font-medium text-muted-foreground">
                  {day}
                </div>
              ))}
            </div>

            {/* Calendar Grid */}
            <div className="grid grid-cols-7 gap-2">
              {/* Empty cells for days before the first day of month */}
              {Array.from({ length: firstDayOfMonth }).map((_, index) => (
                <div key={`empty-${index}`} className="aspect-square" />
              ))}
              
              {/* Days of the month */}
              {Array.from({ length: daysInMonth }).map((_, index) => {
                const day = index + 1;
                const posts = scheduledPosts[day] || [];
                
                return (
                  <div
                    key={day}
                    className={cn(
                      "aspect-square p-2 rounded-xl border transition-colors cursor-pointer",
                      isToday(day)
                        ? "bg-primary/10 border-primary"
                        : "border-border hover:border-primary/50 hover:bg-secondary/50"
                    )}
                  >
                    <div className={cn(
                      "text-sm font-medium mb-1",
                      isToday(day) ? "text-primary" : "text-foreground"
                    )}>
                      {day}
                    </div>
                    
                    <div className="space-y-1">
                      {posts.slice(0, 2).map((post) => (
                        <div
                          key={post.id}
                          className={cn(
                            "w-full h-1.5 rounded-full",
                            platformColors[post.platform]
                          )}
                          title={post.title}
                        />
                      ))}
                      {posts.length > 2 && (
                        <div className="text-[10px] text-muted-foreground">
                          +{posts.length - 2} más
                        </div>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        </div>

        {/* Sidebar - Upcoming Posts */}
        <div className="space-y-4">
          <div className="glass rounded-2xl p-6">
            <h3 className="font-semibold text-foreground mb-4">Próximas Publicaciones</h3>
            
            <div className="space-y-3">
              {Object.entries(scheduledPosts)
                .flatMap(([day, posts]) => 
                  posts.map(post => ({ ...post, day: parseInt(day) }))
                )
                .slice(0, 5)
                .map((post) => (
                  <div
                    key={post.id}
                    className="p-3 rounded-xl bg-secondary/50 hover:bg-secondary transition-colors"
                  >
                    <div className="flex items-start gap-3">
                      <div className={cn(
                        "w-8 h-8 rounded-lg flex items-center justify-center flex-shrink-0",
                        platformColors[post.platform]
                      )}>
                        {post.platform === "youtube" && <Youtube className="w-4 h-4 text-white" />}
                        {post.platform === "instagram" && <Instagram className="w-4 h-4 text-white" />}
                        {post.platform === "tiktok" && (
                          <svg className="w-4 h-4 text-background" viewBox="0 0 24 24" fill="currentColor">
                            <path d="M19.59 6.69a4.83 4.83 0 0 1-3.77-4.25V2h-3.45v13.67a2.89 2.89 0 0 1-5.2 1.74 2.89 2.89 0 0 1 2.31-4.64 2.93 2.93 0 0 1 .88.13V9.4a6.84 6.84 0 0 0-1-.05A6.33 6.33 0 0 0 5 20.1a6.34 6.34 0 0 0 10.86-4.43v-7a8.16 8.16 0 0 0 4.77 1.52v-3.4a4.85 4.85 0 0 1-1-.1z" />
                          </svg>
                        )}
                      </div>
                      <div className="flex-1 min-w-0">
                        <p className="text-sm font-medium text-foreground truncate">
                          {post.title}
                        </p>
                        <p className="text-xs text-muted-foreground">
                          Día {post.day} • {post.time}
                        </p>
                      </div>
                    </div>
                  </div>
                ))}
            </div>
          </div>

          {/* Platform Legend */}
          <div className="glass rounded-2xl p-6">
            <h3 className="font-semibold text-foreground mb-4">Plataformas</h3>
            <div className="space-y-3">
              <div className="flex items-center gap-3">
                <div className="w-4 h-4 rounded bg-red-500" />
                <span className="text-sm text-muted-foreground">YouTube</span>
              </div>
              <div className="flex items-center gap-3">
                <div className="w-4 h-4 rounded bg-gradient-to-br from-purple-500 to-pink-500" />
                <span className="text-sm text-muted-foreground">Instagram</span>
              </div>
              <div className="flex items-center gap-3">
                <div className="w-4 h-4 rounded bg-foreground" />
                <span className="text-sm text-muted-foreground">TikTok</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </MainLayout>
  );
};

export default CalendarPage;
