require 'rspec'

RSpec.configure do |config|
    config.formatter = :html # Pour afficher des détails pour tous les tests
    config.color_mode = :on # Activer la coloration pour une meilleure lisibilité
    config.warnings = true # Activer les warnings
    config.raise_errors_for_deprecations! # Afficher les erreurs pour les méthodes dépréciées
end
  