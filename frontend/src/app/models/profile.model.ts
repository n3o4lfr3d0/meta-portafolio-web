export interface SocialLink {
  name: string;
  url: string;
  icon: string;
}

export interface Profile {
  name: string;
  title: string;
  summary: string;
  location: string;
  experienceYears: string;
  specialization: string;
  socialLinks: SocialLink[];
}
