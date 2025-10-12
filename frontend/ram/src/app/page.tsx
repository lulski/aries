"use client";
import { useToggle } from "@mantine/hooks";
import AboutMePage from "./about/page";
import { AriesThemeSet } from "./theme";

export default function Aries() {
  const [themeValue, themeToggle] = useToggle(AriesThemeSet.colors);

  return <AboutMePage />;
}
