"use client";
import React, {
  createContext,
  useState,
  useContext,
  ReactNode,
  useEffect,
  useCallback,
} from "react";
import { MantineProvider } from "@mantine/core";
import { AriesThemeSet, createAriesTheme } from "../theme";

type ThemeContextType = {
  primaryColor: (typeof AriesThemeSet.colors)[number];
  setPrimaryColor: (color: (typeof AriesThemeSet.colors)[number]) => void;
};

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export const useTheme = (): ThemeContextType => {
  const context = useContext(ThemeContext);
  if (context === undefined) {
    throw new Error("useTheme must be used within a ThemeProvider");
  }
  return context;
};

export const ThemeProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [primaryColor, setPrimaryColorState] = useState<
    (typeof AriesThemeSet.colors)[number]
  >(AriesThemeSet.colors[0]!);

  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Load the saved color from localStorage when the component mounts
    const savedColor = localStorage.getItem("primaryColor");
    if (savedColor && AriesThemeSet.colors.includes(savedColor as any)) {
      setPrimaryColor(savedColor as (typeof AriesThemeSet.colors)[number]);
    }
    setIsLoading(false);
  }, []);

  const setPrimaryColor = useCallback(
    (color: (typeof AriesThemeSet.colors)[number]) => {
      setPrimaryColorState(color);
      localStorage.setItem("primaryColor", color);
    },
    []
  );

  const contextValue: ThemeContextType = {
    primaryColor,
    setPrimaryColor,
  };

  if (isLoading) {
    return null; // or a loading spinner
  }

  return (
    <ThemeContext.Provider value={contextValue}>
      <MantineProvider theme={createAriesTheme(primaryColor)}>
        {children}
      </MantineProvider>
    </ThemeContext.Provider>
  );
};
