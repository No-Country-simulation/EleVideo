import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';
import { useTheme } from '@/context/ThemeContext';
import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Sheet, SheetContent, SheetTrigger } from '@/components/ui/sheet';
import {
  LayoutDashboard,
  User,
  LogOut,
  Moon,
  Sun,
  Menu,
  Film,
  Sparkles,
  Bell,
} from 'lucide-react';
import { cn } from '@/lib/utils';
import { useEffect, useState } from 'react';
import { requestNotificationPermission } from '@/lib/notifications';
import { toast } from 'sonner';

const navItems = [
  { href: '/dashboard', label: 'Proyectos', icon: LayoutDashboard },
];

export function Layout({ children }) {
  const { user, logout } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();
  const location = useLocation();
  const [notificationsEnabled, setNotificationsEnabled] = useState(false);

  useEffect(() => {
    setNotificationsEnabled(Notification.permission === 'granted');
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleEnableNotifications = async () => {
    const granted = await requestNotificationPermission();
    setNotificationsEnabled(granted);
    if (granted) {
      toast.success('Notificaciones activadas');
    } else {
      toast.error('No se pudieron activar las notificaciones');
    }
  };

  return (
    <div className="min-h-screen dashboard-bg">
      {/* Header */}
      <header className="sticky top-0 z-50 w-full border-b border-border/50 bg-background/80 backdrop-blur-xl">
        <div className="container flex h-16 items-center justify-between px-4 md:px-8">
          {/* Logo */}
          <Link to="/dashboard" className="flex items-center gap-3 group">
            <div className="relative">
              <div className="absolute inset-0 bg-gradient-to-r from-blue-500 to-purple-500 rounded-xl blur-lg opacity-50 group-hover:opacity-75 transition-opacity" />
              <div className="relative p-2 rounded-xl bg-gradient-to-br from-blue-500 to-purple-600">
                <Film className="h-5 w-5 text-white" />
              </div>
            </div>
            <div className="flex flex-col">
              <span className="font-outfit text-xl font-bold tracking-tight gradient-text">
                Elevideo
              </span>
              <span className="text-[10px] text-muted-foreground -mt-1 hidden sm:block">
                Smart Video Converter
              </span>
            </div>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex items-center gap-1">
            {navItems.map((item) => (
              <Link
                key={item.href}
                to={item.href}
                className={cn(
                  'flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all',
                  location.pathname === item.href 
                    ? 'bg-accent/10 text-accent' 
                    : 'text-muted-foreground hover:text-foreground hover:bg-muted'
                )}
              >
                <item.icon className="h-4 w-4" />
                {item.label}
              </Link>
            ))}
          </nav>

          {/* Actions */}
          <div className="flex items-center gap-2">
            {/* Notifications Toggle */}
            <Button
              variant="ghost"
              size="icon"
              onClick={handleEnableNotifications}
              className={cn(
                "relative",
                notificationsEnabled && "text-accent"
              )}
              data-testid="notifications-toggle"
            >
              <Bell className="h-5 w-5" />
              {notificationsEnabled && (
                <span className="absolute top-1 right-1 w-2 h-2 bg-green-500 rounded-full" />
              )}
            </Button>

            {/* Theme Toggle */}
            <Button
              variant="ghost"
              size="icon"
              onClick={toggleTheme}
              data-testid="theme-toggle"
              className="relative overflow-hidden"
            >
              <div className={cn(
                "transition-transform duration-300",
                theme === 'dark' ? "rotate-0" : "rotate-180"
              )}>
                {theme === 'light' ? (
                  <Moon className="h-5 w-5" />
                ) : (
                  <Sun className="h-5 w-5 text-yellow-400" />
                )}
              </div>
            </Button>

            {/* User Menu */}
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button 
                  variant="ghost" 
                  className="flex items-center gap-2 pl-2 pr-3"
                  data-testid="user-menu-trigger"
                >
                  <div className="w-8 h-8 rounded-full bg-gradient-to-br from-blue-500 to-purple-600 flex items-center justify-center">
                    <span className="text-white text-sm font-medium">
                      {user?.firstName?.[0]}{user?.lastName?.[0]}
                    </span>
                  </div>
                  <span className="hidden sm:block text-sm font-medium">
                    {user?.firstName}
                  </span>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-56">
                <div className="px-3 py-2">
                  <p className="font-medium">{user?.firstName} {user?.lastName}</p>
                  <p className="text-xs text-muted-foreground truncate">{user?.email}</p>
                </div>
                <DropdownMenuSeparator />
                <DropdownMenuItem asChild>
                  <Link to="/profile" className="cursor-pointer" data-testid="profile-link">
                    <User className="mr-2 h-4 w-4" />
                    Mi Perfil
                  </Link>
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DropdownMenuItem
                  onClick={handleLogout}
                  className="cursor-pointer text-destructive focus:text-destructive"
                  data-testid="logout-button"
                >
                  <LogOut className="mr-2 h-4 w-4" />
                  Cerrar sesión
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>

            {/* Mobile Menu */}
            <Sheet>
              <SheetTrigger asChild className="md:hidden">
                <Button variant="ghost" size="icon" data-testid="mobile-menu-trigger">
                  <Menu className="h-5 w-5" />
                </Button>
              </SheetTrigger>
              <SheetContent side="left" className="w-72">
                <div className="flex flex-col gap-8 py-6">
                  <Link to="/dashboard" className="flex items-center gap-3">
                    <div className="p-2 rounded-xl bg-gradient-to-br from-blue-500 to-purple-600">
                      <Film className="h-5 w-5 text-white" />
                    </div>
                    <span className="font-outfit text-xl font-bold gradient-text">Elevideo</span>
                  </Link>
                  <nav className="flex flex-col gap-2">
                    {navItems.map((item) => (
                      <Link
                        key={item.href}
                        to={item.href}
                        className={cn(
                          'flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-all',
                          location.pathname === item.href 
                            ? 'bg-accent/10 text-accent' 
                            : 'text-muted-foreground hover:text-foreground hover:bg-muted'
                        )}
                      >
                        <item.icon className="h-5 w-5" />
                        {item.label}
                      </Link>
                    ))}
                  </nav>
                </div>
              </SheetContent>
            </Sheet>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="container px-4 py-8 md:px-8 md:py-12">
        {children}
      </main>
    </div>
  );
}
