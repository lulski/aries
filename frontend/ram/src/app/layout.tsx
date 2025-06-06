import "@mantine/core/styles.css";
import "./globals.css";

import React from "react";
import {
  ColorSchemeScript,
  mantineHtmlProps,
  MantineProvider,
} from "@mantine/core";

import AriesLayout from "@/app/components/AriesLayout";
import { ThemeProvider } from "./context/ThemeContext";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" {...mantineHtmlProps}>
      <head>
        <ColorSchemeScript defaultColorScheme="auto" />
      </head>
      <meta
        name="viewport"
        content="minimum-scale=1, initial-scale=1, width=device-width, user-scalable=no"
      />
      <body>
        <MantineProvider defaultColorScheme="auto">
          <ThemeProvider>
            <AriesLayout>{children}</AriesLayout>
          </ThemeProvider>
        </MantineProvider>
      </body>
    </html>
  );
}
