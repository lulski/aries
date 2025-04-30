import { ReactNode } from "react";

export interface NavbarItem {
  label: string;
  href: string;
}

export interface AriesLayoutProps {
  navbarItems?: NavbarItem[];
  selectedTheme?: string;
}
