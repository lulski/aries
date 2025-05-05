"use client";
import React, { createContext, useState, useContext, ReactNode } from "react";
import { MantineProvider } from "@mantine/core";
import { AriesThemeSet, createAriesTheme } from "../theme";

type ThemeContextType = {
  primaryColor: (typeof AriesThemeSet.colors)[number];
  setPrimaryColor: (color: (typeof AriesThemeSet.colors)[number]) => void;
};

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export const ThemeProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [primaryColor, setPrimaryColor] = useState<
    (typeof AriesThemeSet.colors)[number]
  >(AriesThemeSet.colors[0]!);

  const contextValue: ThemeContextType = {
    primaryColor,
    setPrimaryColor,
  };

  return (
    <ThemeContext.Provider value={contextValue}>
      <MantineProvider theme={createAriesTheme(primaryColor)}>
        {children}
      </MantineProvider>
    </ThemeContext.Provider>
  );
};

export const useTheme = (): ThemeContextType => {
  const context = useContext(ThemeContext);
  if (context === undefined) {
    throw new Error("useTheme must be used within a ThemeProvider");
  }
  return context;
};
