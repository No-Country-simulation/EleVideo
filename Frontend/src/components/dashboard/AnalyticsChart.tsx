import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts";

const data = [
  { name: "Lun", views: 4000, engagement: 2400 },
  { name: "Mar", views: 3000, engagement: 1398 },
  { name: "Mié", views: 2000, engagement: 9800 },
  { name: "Jue", views: 2780, engagement: 3908 },
  { name: "Vie", views: 1890, engagement: 4800 },
  { name: "Sáb", views: 2390, engagement: 3800 },
  { name: "Dom", views: 3490, engagement: 4300 },
];

export const AnalyticsChart = () => {
  return (
    <div className="glass rounded-2xl p-6 animate-slide-up">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h3 className="text-lg font-semibold text-foreground">Rendimiento Semanal</h3>
          <p className="text-sm text-muted-foreground">Vistas y engagement de tus videos</p>
        </div>
        <div className="flex items-center gap-4">
          <div className="flex items-center gap-2">
            <div className="w-3 h-3 rounded-full bg-primary" />
            <span className="text-sm text-muted-foreground">Vistas</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-3 h-3 rounded-full bg-accent" />
            <span className="text-sm text-muted-foreground">Engagement</span>
          </div>
        </div>
      </div>

      <div className="h-[300px]">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={data}>
            <defs>
              <linearGradient id="colorViews" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="hsl(220, 70%, 55%)" stopOpacity={0.3} />
                <stop offset="95%" stopColor="hsl(220, 70%, 55%)" stopOpacity={0} />
              </linearGradient>
              <linearGradient id="colorEngagement" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="hsl(16, 85%, 55%)" stopOpacity={0.3} />
                <stop offset="95%" stopColor="hsl(16, 85%, 55%)" stopOpacity={0} />
              </linearGradient>
            </defs>
            <CartesianGrid strokeDasharray="3 3" stroke="hsl(220, 20%, 18%)" />
            <XAxis 
              dataKey="name" 
              stroke="hsl(220, 10%, 55%)"
              fontSize={12}
              tickLine={false}
              axisLine={false}
            />
            <YAxis 
              stroke="hsl(220, 10%, 55%)"
              fontSize={12}
              tickLine={false}
              axisLine={false}
              tickFormatter={(value) => `${value / 1000}k`}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: "hsl(220, 25%, 12%)",
                border: "1px solid hsl(220, 20%, 18%)",
                borderRadius: "12px",
                boxShadow: "0 10px 15px -3px rgba(0, 0, 0, 0.4)",
              }}
              labelStyle={{ color: "hsl(220, 10%, 95%)" }}
            />
            <Area
              type="monotone"
              dataKey="views"
              stroke="hsl(220, 70%, 55%)"
              strokeWidth={2}
              fillOpacity={1}
              fill="url(#colorViews)"
            />
            <Area
              type="monotone"
              dataKey="engagement"
              stroke="hsl(16, 85%, 55%)"
              strokeWidth={2}
              fillOpacity={1}
              fill="url(#colorEngagement)"
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};
