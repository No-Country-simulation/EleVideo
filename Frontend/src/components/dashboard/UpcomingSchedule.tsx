import { Calendar, Clock, Youtube, Instagram } from "lucide-react";

interface ScheduleItem {
  id: number;
  title: string;
  platform: "youtube" | "instagram" | "tiktok";
  time: string;
  date: string;
  type: "horizontal" | "short";
}

const scheduleItems: ScheduleItem[] = [
  {
    id: 1,
    title: "Tutorial de Marketing Digital 2024",
    platform: "youtube",
    time: "10:00 AM",
    date: "Hoy",
    type: "horizontal",
  },
  {
    id: 2,
    title: "5 Tips para emprendedores",
    platform: "instagram",
    time: "2:00 PM",
    date: "Hoy",
    type: "short",
  },
  {
    id: 3,
    title: "Detrás de cámaras",
    platform: "tiktok",
    time: "6:00 PM",
    date: "Mañana",
    type: "short",
  },
  {
    id: 4,
    title: "Cómo conseguir más clientes",
    platform: "youtube",
    time: "9:00 AM",
    date: "Mañana",
    type: "horizontal",
  },
];

const platformIcons = {
  youtube: Youtube,
  instagram: Instagram,
  tiktok: () => (
    <svg className="w-4 h-4" viewBox="0 0 24 24" fill="currentColor">
      <path d="M19.59 6.69a4.83 4.83 0 0 1-3.77-4.25V2h-3.45v13.67a2.89 2.89 0 0 1-5.2 1.74 2.89 2.89 0 0 1 2.31-4.64 2.93 2.93 0 0 1 .88.13V9.4a6.84 6.84 0 0 0-1-.05A6.33 6.33 0 0 0 5 20.1a6.34 6.34 0 0 0 10.86-4.43v-7a8.16 8.16 0 0 0 4.77 1.52v-3.4a4.85 4.85 0 0 1-1-.1z" />
    </svg>
  ),
};

const platformColors = {
  youtube: "text-red-500",
  instagram: "text-pink-500",
  tiktok: "text-foreground",
};

export const UpcomingSchedule = () => {
  return (
    <div className="glass rounded-2xl p-6 animate-slide-up">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h3 className="text-lg font-semibold text-foreground">Próximas Publicaciones</h3>
          <p className="text-sm text-muted-foreground">Tu contenido programado</p>
        </div>
        <button className="text-sm font-medium text-primary hover:text-primary/80 transition-colors">
          Ver todo
        </button>
      </div>

      <div className="space-y-4">
        {scheduleItems.map((item) => {
          const PlatformIcon = platformIcons[item.platform];
          return (
            <div
              key={item.id}
              className="flex items-center gap-4 p-3 rounded-xl bg-secondary/50 hover:bg-secondary transition-colors"
            >
              <div className={`flex-shrink-0 ${platformColors[item.platform]}`}>
                <PlatformIcon />
              </div>
              
              <div className="flex-1 min-w-0">
                <p className="font-medium text-foreground truncate">{item.title}</p>
                <div className="flex items-center gap-3 mt-1 text-xs text-muted-foreground">
                  <span className="flex items-center gap-1">
                    <Calendar className="w-3 h-3" />
                    {item.date}
                  </span>
                  <span className="flex items-center gap-1">
                    <Clock className="w-3 h-3" />
                    {item.time}
                  </span>
                </div>
              </div>

              <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                item.type === "short" 
                  ? "bg-accent/20 text-accent" 
                  : "bg-primary/20 text-primary"
              }`}>
                {item.type === "short" ? "Short" : "Video"}
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
};
