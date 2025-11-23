import "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import Aries from "./page";

jest.mock("@mantine/core", () => ({
  Stack: ({ children }: any) => <div data-testid="stack">{children}</div>,
  Center: ({ children }: any) => <div data-testid="center">{children}</div>,
  Box: ({ children, ...props }: any) => (
    <div data-testid="box" {...props}>
      {children}
    </div>
  ),
  Button: ({ children, ...props }: any) => (
    <button {...props}>{children}</button>
  ),
  Image: ({ src, ...props }: any) => <img src={src} alt="mock" {...props} />,
  Group: ({ children }: any) => <div data-testid="group">{children}</div>,
  useMantineColorScheme: () => ({
    colorScheme: "light",
    setColorScheme: jest.fn(),
  }),
}));

jest.mock("@mantine/hooks", () => ({
  useToggle: (values: string[]) => [values[0], jest.fn()],
}));

jest.mock("./theme", () => ({
  AriesThemeSet: { colors: ["light", "dark"] },
}));

jest.mock("./about/page", () => {
  return {
    __esModule: true,
    default: () => <div data-testid="about-page">About me</div>,
  };
});

describe("Aries component", () => {
  it("renders AboutMePage", () => {
    render(<Aries />);
    expect(screen.getByTestId("about-page")).toBeInTheDocument();
  });
});
