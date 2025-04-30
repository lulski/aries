"use client";

import "@mantine/core/styles.css";
import "./globals.css";

import React from "react";
import {
  ColorSchemeScript,
  MantineProvider,
  mantineHtmlProps,
} from "@mantine/core";

import AriesLayout from "@/app/components/AriesLayout";
import { AriesLayoutProps } from "@/app/types/AriesLayoutProperties";
import { theme } from "./theme";
import { useToggle } from "@mantine/hooks";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const layoutProps: AriesLayoutProps = {
    navbarItems: [
      { label: "Home", href: "/" },
      { label: "Posts", href: "/posts" },
      { label: "About", href: "/about" },
    ],
  };

  return (
    <html lang="en" {...mantineHtmlProps}>
      <head>
        <ColorSchemeScript />
      </head>
      <meta
        name="viewport"
        content="minimum-scale=1, initial-scale=1, width=device-width, user-scalable=no"
      />
      <body>
        <MantineProvider theme={theme} defaultColorScheme="light">
          <AriesLayout {...layoutProps}>{children}</AriesLayout>
        </MantineProvider>
      </body>
    </html>
  );
}
